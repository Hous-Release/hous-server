package hous.server.service.badge;

import hous.server.common.exception.ForbiddenException;
import hous.server.common.exception.NotFoundException;
import hous.server.domain.badge.Badge;
import hous.server.domain.badge.BadgeInfo;
import hous.server.domain.badge.repository.AcquireRepository;
import hous.server.domain.badge.repository.BadgeRepository;
import hous.server.domain.user.Onboarding;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static hous.server.common.exception.ErrorCode.FORBIDDEN_ACQUIRE_BADGE_EXCEPTION;
import static hous.server.common.exception.ErrorCode.NOT_FOUND_BADGE_EXCEPTION;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BadgeServiceUtils {

    public static boolean hasBadge(BadgeRepository badgeRepository, AcquireRepository acquireRepository,
                                   BadgeInfo badgeInfo, Onboarding onboarding) {
        Badge badge = badgeRepository.findBadgeByBadgeInfo(badgeInfo);
        if (badge == null) {
            throw new NotFoundException(String.format("존재하지 않는 badge (%s) 입니다", badgeInfo.getValue()), NOT_FOUND_BADGE_EXCEPTION);
        }
        return acquireRepository.existsByOnboardingAndBadge(onboarding, badge);
    }

    public static Badge findBadgeById(BadgeRepository badgeRepository, Long badgeId) {
        Badge badge = badgeRepository.findBadgeByBadgeId(badgeId);
        if (badge == null) {
            throw new NotFoundException(String.format("존재하지 않는 badge (%s) 입니다", badgeId), NOT_FOUND_BADGE_EXCEPTION);
        }
        return badge;
    }

    public static void validateExistsByOnboardingAndBadge(AcquireRepository acquireRepository, Onboarding onboarding, Badge badge) {
        if (!acquireRepository.existsByOnboardingAndBadge(onboarding, badge)) {
            throw new ForbiddenException(String.format("유저가 (%s) 획득한 badge (%s) 가 아닙니다.", onboarding.getId(), badge.getId()), FORBIDDEN_ACQUIRE_BADGE_EXCEPTION);
        }
    }
}
