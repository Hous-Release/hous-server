package hous.api.service.auth;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import hous.api.service.auth.impl.AppleAuthService;
import hous.api.service.auth.impl.KakaoAuthService;
import hous.core.domain.user.UserSocialType;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class AuthServiceProvider {

	private static final Map<UserSocialType, AuthService> authServiceMap = new HashMap<>();

	private final AppleAuthService appleAuthService;
	private final KakaoAuthService kakaoAuthService;

	@PostConstruct
	void initializeAuthServicesMap() {
		authServiceMap.put(UserSocialType.APPLE, appleAuthService);
		authServiceMap.put(UserSocialType.KAKAO, kakaoAuthService);
	}

	public AuthService getAuthService(UserSocialType socialType) {
		return authServiceMap.get(socialType);
	}
}
