package hous.api.service.jwt;

import hous.api.config.security.JwtConstants;
import hous.api.service.auth.dto.response.TokenResponse;
import hous.common.constant.RedisKey;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.DecodingException;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class JwtService {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final long ACCESS_TOKEN_EXPIRE_TIME = 10 * 60 * 1000L;              // 10분
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 6 * 30 * 24 * 60 * 60 * 1000L;    // 180일
    //    private static final long ACCESS_TOKEN_EXPIRE_TIME = 365 * 24 * 60 * 60 * 1000L;   // 1년
    //    private static final long REFRESH_TOKEN_EXPIRE_TIME = 365 * 24 * 60 * 60 * 1000L;    // 1년
    private static final long EXPIRED_TIME = 1L;

    private final Key secretKey;

    public JwtService(@Value("${jwt.secret}") String secretKey, RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public TokenResponse createTokenInfo(Long userId) {

        long now = (new Date()).getTime();
        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        Date refreshTokenExpiresIn = new Date(now + REFRESH_TOKEN_EXPIRE_TIME);

        // Access Token 생성
        String accessToken = Jwts.builder()
                .claim(JwtConstants.USER_ID, userId)
                .setExpiration(accessTokenExpiresIn)
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();

        // Refresh Token 생성
        String refreshToken = Jwts.builder()
                .setExpiration(refreshTokenExpiresIn)
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();

        redisTemplate.opsForValue()
                .set(RedisKey.REFRESH_TOKEN + userId, refreshToken, REFRESH_TOKEN_EXPIRE_TIME, TimeUnit.MILLISECONDS);

        return TokenResponse.of(accessToken, refreshToken);
    }

    public void expireRefreshToken(Long userId) {
        redisTemplate.opsForValue().set(RedisKey.REFRESH_TOKEN + userId, "", EXPIRED_TIME, TimeUnit.MILLISECONDS);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException | DecodingException e) {
            log.warn("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.warn("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.warn("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.warn("JWT claims string is empty.", e);
        } catch (Exception e) {
            log.error("Unhandled JWT exception", e);
        }
        return false;
    }

    public Long getUserIdFromJwt(String accessToken) {
        return parseClaims(accessToken).get(JwtConstants.USER_ID, Long.class);
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
