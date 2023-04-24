package hous.api.service.badge;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hous.api.service.notification.NotificationService;
import hous.core.domain.badge.Acquire;
import hous.core.domain.badge.BadgeInfo;
import hous.core.domain.badge.mysql.AcquireRepository;
import hous.core.domain.badge.mysql.BadgeRepository;
import hous.core.domain.user.User;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional
public class BadgeService {

	private final BadgeRepository badgeRepository;
	private final AcquireRepository acquireRepository;

	private final NotificationService notificationService;

	public void acquireBadge(User user, BadgeInfo badgeInfo) {
		if (!BadgeServiceUtils.hasBadge(badgeRepository, acquireRepository, badgeInfo, user.getOnboarding())) {
			Acquire acquire = acquireRepository.save(
				Acquire.newInstance(user.getOnboarding(), badgeRepository.findBadgeByBadgeInfo(badgeInfo)));
			user.getOnboarding().addAcquire(acquire);
			notificationService.sendNewBadgeNotification(user, badgeInfo);
		}
	}
}
