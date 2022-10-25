package hous.server.service.auth.impl;

import hous.server.common.exception.ConflictException;
import hous.server.common.util.HttpHeaderUtils;
import hous.server.common.util.JwtUtils;
import hous.server.domain.common.RedisKey;
import hous.server.domain.user.User;
import hous.server.domain.user.UserSocialType;
import hous.server.domain.user.repository.UserRepository;
import hous.server.external.client.kakao.KakaoApiClient;
import hous.server.external.client.kakao.dto.response.KakaoProfileResponse;
import hous.server.service.auth.AuthService;
import hous.server.service.auth.dto.request.LoginDto;
import hous.server.service.auth.dto.request.SignUpDto;
import hous.server.service.user.UserService;
import hous.server.service.user.UserServiceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static hous.server.common.exception.ErrorCode.CONFLICT_USER_EXCEPTION;

@RequiredArgsConstructor
@Service
@Transactional
public class KakaoAuthService implements AuthService {

    private static final UserSocialType socialType = UserSocialType.KAKAO;

    private final KakaoApiClient kakaoApiCaller;

    private final UserRepository userRepository;

    private final UserService userService;

    private final JwtUtils jwtProvider;

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public Long signUp(SignUpDto request) {
        KakaoProfileResponse response = kakaoApiCaller.getProfileInfo(HttpHeaderUtils.withBearerToken(request.getToken()));
        return userService.registerUser(request.toCreateUserDto(response.getId()));
    }

    @Override
    public Long login(LoginDto request) {
        KakaoProfileResponse response = kakaoApiCaller.getProfileInfo(HttpHeaderUtils.withBearerToken(request.getToken()));
        User user = UserServiceUtils.findUserBySocialIdAndSocialType(userRepository, response.getId(), socialType);
        User conflictFcmTokenUser = userRepository.findUserByFcmToken(request.getFcmToken());
        if (conflictFcmTokenUser != null) {
            jwtProvider.expireRefreshToken(conflictFcmTokenUser.getId());
            conflictFcmTokenUser.resetFcmToken();
        }
        String refreshToken = (String) redisTemplate.opsForValue().get(RedisKey.REFRESH_TOKEN + user.getId());
        if (refreshToken != null) {
            throw new ConflictException(String.format("이미 로그인된 유저 (%s) 입니다.", user.getId()), CONFLICT_USER_EXCEPTION);
        }
        user.updateFcmToken(request.getFcmToken());
        return user.getId();
    }

    @Override
    public Long forceLogin(LoginDto request) {
        KakaoProfileResponse response = kakaoApiCaller.getProfileInfo(HttpHeaderUtils.withBearerToken(request.getToken()));
        User user = UserServiceUtils.findUserBySocialIdAndSocialType(userRepository, response.getId(), socialType);
        User conflictFcmTokenUser = userRepository.findUserByFcmToken(request.getFcmToken());
        if (conflictFcmTokenUser != null) {
            jwtProvider.expireRefreshToken(conflictFcmTokenUser.getId());
            conflictFcmTokenUser.resetFcmToken();
        }
        String refreshToken = (String) redisTemplate.opsForValue().get(RedisKey.REFRESH_TOKEN + user.getId());
        if (refreshToken != null) {
            jwtProvider.expireRefreshToken(user.getId());
            user.resetFcmToken();
        }
        user.updateFcmToken(request.getFcmToken());
        return user.getId();
    }
}
