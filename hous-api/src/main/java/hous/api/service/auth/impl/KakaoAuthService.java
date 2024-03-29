package hous.api.service.auth.impl;

import javax.transaction.Transactional;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import hous.api.service.auth.AuthService;
import hous.api.service.auth.CommonAuthService;
import hous.api.service.auth.CommonAuthServiceUtils;
import hous.api.service.auth.dto.request.LoginDto;
import hous.api.service.auth.dto.request.SignUpDto;
import hous.api.service.user.UserService;
import hous.api.service.user.UserServiceUtils;
import hous.common.util.HttpHeaderUtils;
import hous.common.util.JwtUtils;
import hous.core.domain.user.User;
import hous.core.domain.user.UserSocialType;
import hous.core.domain.user.mysql.UserRepository;
import hous.external.client.kakao.KakaoApiClient;
import hous.external.client.kakao.dto.response.KakaoProfileResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional
public class KakaoAuthService implements AuthService {

	private static final UserSocialType socialType = UserSocialType.KAKAO;

	private final KakaoApiClient kakaoApiCaller;

	private final UserRepository userRepository;

	private final UserService userService;

	private final CommonAuthService commonAuthService;

	private final JwtUtils jwtUtils;

	private final RedisTemplate<String, Object> redisTemplate;

	@Override
	public Long signUp(SignUpDto request) {
		KakaoProfileResponse response = kakaoApiCaller.getProfileInfo(
			HttpHeaderUtils.withBearerToken(request.getToken()));
		commonAuthService.resetConflictFcmToken(request.getFcmToken());
		return userService.registerUser(request.toCreateUserDto(response.getId()));
	}

	@Override
	public Long login(LoginDto request) {
		KakaoProfileResponse response = kakaoApiCaller.getProfileInfo(
			HttpHeaderUtils.withBearerToken(request.getToken()));
		User user = UserServiceUtils.findUserBySocialIdAndSocialType(userRepository, response.getId(), socialType);
		commonAuthService.resetConflictFcmToken(request.getFcmToken());
		CommonAuthServiceUtils.validateUniqueLogin(redisTemplate, user);
		user.updateFcmToken(request.getFcmToken());
		return user.getId();
	}

	@Override
	public Long forceLogin(LoginDto request) {
		KakaoProfileResponse response = kakaoApiCaller.getProfileInfo(
			HttpHeaderUtils.withBearerToken(request.getToken()));
		User user = UserServiceUtils.findUserBySocialIdAndSocialType(userRepository, response.getId(), socialType);
		commonAuthService.resetConflictFcmToken(request.getFcmToken());
		CommonAuthServiceUtils.forceLogoutUser(redisTemplate, jwtUtils, user);
		user.updateFcmToken(request.getFcmToken());
		return user.getId();
	}
}
