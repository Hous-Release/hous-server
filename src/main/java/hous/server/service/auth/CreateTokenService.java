package hous.server.service.auth;

import hous.server.common.exception.UnAuthorizedException;
import hous.server.common.util.JwtUtils;
import hous.server.service.auth.dto.request.TokenRequestDto;
import hous.server.service.auth.dto.response.TokenResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@RequiredArgsConstructor
@Service
public class CreateTokenService {

    private final JwtUtils jwtProvider;

    private final RedisTemplate redisTemplate;

    @Transactional
    public TokenResponseDto createTokenInfo(Long userId) {
        return jwtProvider.createTokenInfo(userId);
    }

    @Transactional
    public TokenResponseDto reissueToken(TokenRequestDto request) {
        if (!jwtProvider.validateToken(request.getRefreshToken())) {
            throw new UnAuthorizedException(String.format("주어진 리프레시 토큰 (%s) 이 유효하지 않습니다.", request.getRefreshToken()));
        }
        Long userId = jwtProvider.getUserIdFromJwt(request.getAccessToken());
        String refreshToken = (String) redisTemplate.opsForValue().get("RT:" + userId);

        if (Objects.isNull(refreshToken)) {
            throw new UnAuthorizedException(String.format("이미 만료된 리프레시 토큰 (%s) 입니다.", request.getRefreshToken()));
        }
        if (!refreshToken.equals(request.getRefreshToken())) {
            throw new UnAuthorizedException(String.format("해당 리프레시 토큰의 정보가 일치하지 않습니다.", request.getRefreshToken()));
        }
        return jwtProvider.createTokenInfo(userId);
    }
}
