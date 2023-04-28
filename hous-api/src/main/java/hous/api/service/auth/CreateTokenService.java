package hous.api.service.auth;

import java.util.List;
import java.util.Objects;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hous.api.service.auth.dto.request.TokenRequestDto;
import hous.api.service.auth.dto.response.RefreshResponse;
import hous.api.service.auth.dto.response.TokenResponse;
import hous.api.service.room.RoomService;
import hous.api.service.user.UserServiceUtils;
import hous.common.constant.RedisKey;
import hous.common.exception.UnAuthorizedException;
import hous.common.util.JwtUtils;
import hous.core.domain.user.User;
import hous.core.domain.user.mysql.UserRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CreateTokenService {

	private final UserRepository userRepository;

	private final RedisTemplate redisTemplate;

	private final JwtUtils jwtUtils;
	private final RoomService roomService;

	@Transactional
	public TokenResponse createTokenInfo(Long userId) {
		List<String> tokens = jwtUtils.createTokenInfo(userId);
		return TokenResponse.of(tokens.get(0), tokens.get(1));
	}

	@Transactional
	public RefreshResponse reissueToken(TokenRequestDto request) {
		Long userId = jwtUtils.getUserIdFromJwt(request.getAccessToken());
		User user = UserServiceUtils.findUserById(userRepository, userId);
		if (!jwtUtils.validateToken(request.getRefreshToken())) {
			user.resetFcmToken();
			throw new UnAuthorizedException(String.format("주어진 리프레시 토큰 (%s) 이 유효하지 않습니다.", request.getRefreshToken()));
		}
		String refreshToken = (String)redisTemplate.opsForValue().get(RedisKey.REFRESH_TOKEN + userId);
		if (Objects.isNull(refreshToken)) {
			user.resetFcmToken();
			throw new UnAuthorizedException(String.format("이미 만료된 리프레시 토큰 (%s) 입니다.", request.getRefreshToken()));
		}
		if (!refreshToken.equals(request.getRefreshToken())) {
			jwtUtils.expireRefreshToken(user.getId());
			user.resetFcmToken();
			throw new UnAuthorizedException(
				String.format("해당 리프레시 토큰 (%s) 의 정보가 일치하지 않습니다.", request.getRefreshToken()));
		}
		List<String> tokens = jwtUtils.createTokenInfo(userId);
		TokenResponse token = TokenResponse.of(tokens.get(0), tokens.get(1));
		boolean isJoiningRoom = roomService.existsParticipatingRoomByUserId(userId);
		return RefreshResponse.of(token, isJoiningRoom);
	}
}
