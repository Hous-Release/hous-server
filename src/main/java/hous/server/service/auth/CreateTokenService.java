package hous.server.service.auth;

import hous.server.common.exception.UnAuthorizedException;
import hous.server.common.util.JwtUtils;
import hous.server.domain.common.RedisKey;
import hous.server.domain.user.User;
import hous.server.domain.user.repository.UserRepository;
import hous.server.service.auth.dto.request.TokenRequestDto;
import hous.server.service.auth.dto.response.RefreshResponse;
import hous.server.service.auth.dto.response.TokenResponse;
import hous.server.service.room.RoomService;
import hous.server.service.user.UserServiceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@RequiredArgsConstructor
@Service
public class CreateTokenService {

    private final UserRepository userRepository;

    private final JwtUtils jwtProvider;

    private final RedisTemplate redisTemplate;

    private final RoomService roomService;

    @Transactional
    public TokenResponse createTokenInfo(Long userId) {
        return jwtProvider.createTokenInfo(userId);
    }

    @Transactional
    public RefreshResponse reissueToken(TokenRequestDto request) {
        Long userId = jwtProvider.getUserIdFromJwt(request.getAccessToken());
        User user = UserServiceUtils.findUserById(userRepository, userId);
        if (!jwtProvider.validateToken(request.getRefreshToken())) {
            user.resetFcmToken();
            throw new UnAuthorizedException(String.format("주어진 리프레시 토큰 (%s) 이 유효하지 않습니다.", request.getRefreshToken()));
        }
        String refreshToken = (String) redisTemplate.opsForValue().get(RedisKey.REFRESH_TOKEN + userId);
        if (Objects.isNull(refreshToken)) {
            user.resetFcmToken();
            throw new UnAuthorizedException(String.format("이미 만료된 리프레시 토큰 (%s) 입니다.", request.getRefreshToken()));
        }
        if (!refreshToken.equals(request.getRefreshToken())) {
            jwtProvider.expireRefreshToken(user.getId());
            user.resetFcmToken();
            throw new UnAuthorizedException(String.format("해당 리프레시 토큰 (%s) 의 정보가 일치하지 않습니다.", request.getRefreshToken()));
        }
        TokenResponse token = jwtProvider.createTokenInfo(userId);
        boolean isJoiningRoom = roomService.existsParticipatingRoomByUserId(userId);
        return RefreshResponse.of(token, isJoiningRoom);
    }
}
