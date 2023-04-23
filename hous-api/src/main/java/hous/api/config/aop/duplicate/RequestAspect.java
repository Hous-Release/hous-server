package hous.api.config.aop.duplicate;

import hous.common.constant.RedisKey;
import hous.common.exception.ConflictException;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import static hous.common.exception.ErrorCode.CONFLICT_REQUEST_EXCEPTION;

@Component
@Aspect
@RequiredArgsConstructor
public class RequestAspect {

    private final RedisTemplate<String, Object> redisTemplate;

    @Before("@annotation(hous.api.config.aop.duplicate.PreventDuplicateRequest)")
    public void beforeRequest(final JoinPoint joinPoint) {
        Long userId = (Long) joinPoint.getArgs()[0];
        if (redisTemplate.opsForValue().get(RedisKey.DUPLICATE_REQUEST + userId) != null) {
            throw new ConflictException(String.format("처리 중인 유저 (%s) 의 메서드 (%s) 입니다.", userId, joinPoint.getSignature().getName()),
                    CONFLICT_REQUEST_EXCEPTION);
        }
        redisTemplate.opsForValue().set(RedisKey.DUPLICATE_REQUEST + userId, Long.toString(userId));
    }

    @After("@annotation(hous.api.config.aop.duplicate.PreventDuplicateRequest)")
    public void afterReturningRequest(final JoinPoint joinPoint) {
        Long userId = (Long) joinPoint.getArgs()[0];
        redisTemplate.opsForValue().getAndDelete(RedisKey.DUPLICATE_REQUEST + userId);
    }
}
