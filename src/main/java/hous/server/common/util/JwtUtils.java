package hous.server.common.util;

import hous.server.config.security.JwtConstants;
import hous.server.service.auth.dto.response.TokenResponse;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
@PropertySource(value = "classpath:application-jwt.yml", factory = YamlPropertySourceFactory.class, ignoreResourceNotFound = true)
public class JwtUtils {

    private final RedisTemplate<String, Object> redisTemplate;

    //    private static final long ACCESS_TOKEN_EXPIRE_TIME = 30 * 60 * 1000L;              // 30분
    //    private static final long REFRESH_TOKEN_EXPIRE_TIME = 7 * 24 * 60 * 60 * 1000L;    // 7일
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 365 * 24 * 60 * 60 * 1000L;   // 1년
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 365 * 24 * 60 * 60 * 1000L;    // 1년

    private final Key secretKey;

    public JwtUtils(@Value("${jwt.secret}") String secretKey, RedisTemplate redisTemplate) {
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
                .set("RT:" + userId, refreshToken, REFRESH_TOKEN_EXPIRE_TIME, TimeUnit.MILLISECONDS);

        return TokenResponse.of(accessToken, refreshToken);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
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
