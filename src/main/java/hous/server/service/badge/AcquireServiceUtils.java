package hous.server.service.badge;

import hous.server.common.exception.ForbiddenException;
import hous.server.domain.badge.Badge;
import hous.server.domain.badge.repository.AcquireRepository;
import hous.server.domain.user.Onboarding;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static hous.server.common.exception.ErrorCode.FORBIDDEN_ACQUIRE_BADGE_EXCEPTION;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AcquireServiceUtils {

    public static void existsByOnboardingAndBadge(AcquireRepository acquireRepository, Onboarding onboarding, Badge badge) {
        if (!acquireRepository.existsByOnboardingAndBadge(onboarding, badge)) {
            throw new ForbiddenException(String.format("유저가 (%s) 획득한 badge (%s) 가 아닙니다.", onboarding.getId(), badge.getId()), FORBIDDEN_ACQUIRE_BADGE_EXCEPTION);
        }
    }
}
