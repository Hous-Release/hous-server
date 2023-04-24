package hous.api.config.interceptor.auth;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import hous.api.service.jwt.JwtService;
import hous.common.exception.UnAuthorizedException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class LoginCheckHandler {

	private final JwtService jwtService;

	public Long getUserId(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			String accessToken = bearerToken.substring("Bearer ".length());
			if (jwtService.validateToken(accessToken)) {
				Long userId = jwtService.getUserIdFromJwt(accessToken);
				if (userId != null) {
					return userId;
				}
			}
		}
		throw new UnAuthorizedException(String.format("잘못된 JWT (%s) 입니다.", bearerToken));
	}
}
