package hous.server.common.aspect;

import hous.server.common.exception.ConflictException;
import hous.server.domain.common.RedisKey;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import static hous.server.common.exception.ErrorCode.CONFLICT_REQUEST_EXCEPTION;

@Component
@Aspect
@RequiredArgsConstructor
public class RequestAspect {

    private final RedisTemplate<String, Object> redisTemplate;

    @Before("@annotation(hous.server.common.aspect.PreventDuplicateRequest)")
    public void beforeRequest(final JoinPoint joinPoint) {
        Long userId = (Long) joinPoint.getArgs()[0];
        if (redisTemplate.opsForValue().get(RedisKey.DUPLICATE_REQUEST + userId) != null) {
            throw new ConflictException(String.format("처리 중인 유저 (%s) 의 메서드 (%s) 입니다.", userId, joinPoint.getSignature().getName()),
                    CONFLICT_REQUEST_EXCEPTION);
        }
        redisTemplate.opsForValue().set(RedisKey.DUPLICATE_REQUEST + userId, Long.toString(userId));
    }

    @AfterReturning("@annotation(hous.server.common.aspect.PreventDuplicateRequest)")
    public void afterReturningRequest(final JoinPoint joinPoint) {
        Long userId = (Long) joinPoint.getArgs()[0];
        redisTemplate.opsForValue().getAndDelete(RedisKey.DUPLICATE_REQUEST + userId);
    }
}
