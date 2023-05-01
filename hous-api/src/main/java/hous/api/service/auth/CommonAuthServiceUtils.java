package hous.api.service.auth;

import static hous.common.exception.ErrorCode.*;

import org.springframework.data.redis.core.RedisTemplate;

import hous.common.constant.RedisKey;
import hous.common.exception.ConflictException;
import hous.common.util.JwtUtils;
import hous.core.domain.user.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommonAuthServiceUtils {

	public static void validateUniqueLogin(RedisTemplate<String, Object> redisTemplate, User user) {
		String refreshToken = (String)redisTemplate.opsForValue().get(RedisKey.REFRESH_TOKEN + user.getId());
		if (refreshToken != null) {
			throw new ConflictException(String.format("이미 로그인된 유저 (%s) 입니다.", user.getId()), CONFLICT_LOGIN_EXCEPTION);
		}
	}

	public static void forceLogoutUser(RedisTemplate<String, Object> redisTemplate, JwtUtils jwtService, User user) {
		String refreshToken = (String)redisTemplate.opsForValue().get(RedisKey.REFRESH_TOKEN + user.getId());
		if (refreshToken != null) {
			jwtService.expireRefreshToken(user.getId());
			user.resetFcmToken();
		}
	}
}
