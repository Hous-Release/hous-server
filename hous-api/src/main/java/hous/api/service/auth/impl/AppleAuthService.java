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
import hous.common.util.JwtUtils;
import hous.core.domain.user.User;
import hous.core.domain.user.UserSocialType;
import hous.core.domain.user.mysql.UserRepository;
import hous.external.client.apple.AppleTokenProvider;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional
public class AppleAuthService implements AuthService {

	private static final UserSocialType socialType = UserSocialType.APPLE;

	private final AppleTokenProvider appleTokenDecoder;

	private final UserRepository userRepository;

	private final UserService userService;

	private final CommonAuthService commonAuthService;

	private final JwtUtils jwtUtils;

	private final RedisTemplate<String, Object> redisTemplate;

	@Override
	public Long signUp(SignUpDto request) {
		String socialId = appleTokenDecoder.getSocialIdFromIdToken(request.getToken());
		commonAuthService.resetConflictFcmToken(request.getFcmToken());
		return userService.registerUser(request.toCreateUserDto(socialId));
	}

	@Override
	public Long login(LoginDto request) {
		String socialId = appleTokenDecoder.getSocialIdFromIdToken(request.getToken());
		User user = UserServiceUtils.findUserBySocialIdAndSocialType(userRepository, socialId, socialType);
		commonAuthService.resetConflictFcmToken(request.getFcmToken());
		CommonAuthServiceUtils.validateUniqueLogin(redisTemplate, user);
		user.updateFcmToken(request.getFcmToken());
		return user.getId();
	}

	@Override
	public Long forceLogin(LoginDto request) {
		String socialId = appleTokenDecoder.getSocialIdFromIdToken(request.getToken());
		User user = UserServiceUtils.findUserBySocialIdAndSocialType(userRepository, socialId, socialType);
		commonAuthService.resetConflictFcmToken(request.getFcmToken());
		CommonAuthServiceUtils.forceLogoutUser(redisTemplate, jwtUtils, user);
		user.updateFcmToken(request.getFcmToken());
		return user.getId();
	}
}
