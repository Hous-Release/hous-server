package hous.server.service.auth.impl;

import hous.server.common.util.HttpHeaderUtils;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class KakaoAuthService implements AuthService {

    private static final UserSocialType socialType = UserSocialType.KAKAO;

    private final KakaoApiClient kakaoApiCaller;

    private final UserRepository userRepository;

    private final UserService userService;

    @Override
    public Long signUp(SignUpDto request) {
        KakaoProfileResponse response = kakaoApiCaller.getProfileInfo(HttpHeaderUtils.withBearerToken(request.getToken()));
        return userService.registerUser(request.toCreateUserDto(response.getId()));
    }

    @Override
    public Long login(LoginDto request) {
        KakaoProfileResponse response = kakaoApiCaller.getProfileInfo(HttpHeaderUtils.withBearerToken(request.getToken()));
        User user = UserServiceUtils.findUserBySocialIdAndSocialType(userRepository, response.getId(), socialType);
        UserServiceUtils.validateUniqueFcmToken(userRepository, request.getFcmToken());
        user.updateFcmToken(request.getFcmToken());
        return user.getId();
    }
}