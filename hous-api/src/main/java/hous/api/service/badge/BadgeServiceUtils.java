package hous.api.service.badge;

import static hous.common.exception.ErrorCode.*;

import hous.common.exception.ForbiddenException;
import hous.common.exception.NotFoundException;
import hous.core.domain.badge.Badge;
import hous.core.domain.badge.BadgeInfo;
import hous.core.domain.badge.mysql.AcquireRepository;
import hous.core.domain.badge.mysql.BadgeRepository;
import hous.core.domain.user.Onboarding;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BadgeServiceUtils {

	public static boolean hasBadge(BadgeRepository badgeRepository, AcquireRepository acquireRepository,
		BadgeInfo badgeInfo, Onboarding onboarding) {
		Badge badge = badgeRepository.findBadgeByBadgeInfo(badgeInfo);
		if (badge == null) {
			throw new NotFoundException(String.format("존재하지 않는 badge (%s) 입니다", badgeInfo.getValue()),
				NOT_FOUND_BADGE_EXCEPTION);
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

	public static void validateExistsByOnboardingAndBadge(AcquireRepository acquireRepository, Onboarding onboarding,
		Badge badge) {
		if (!acquireRepository.existsByOnboardingAndBadge(onboarding, badge)) {
			throw new ForbiddenException(
				String.format("유저가 (%s) 획득한 badge (%s) 가 아닙니다.", onboarding.getId(), badge.getId()),
				FORBIDDEN_ACQUIRE_BADGE_EXCEPTION);
		}
	}
}
