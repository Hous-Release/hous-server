package hous.server.service.badge;

import hous.server.domain.badge.Acquire;
import hous.server.domain.badge.BadgeInfo;
import hous.server.domain.badge.mysql.AcquireRepository;
import hous.server.domain.badge.mysql.BadgeRepository;
import hous.server.domain.user.User;
import hous.server.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class BadgeService {

    private final BadgeRepository badgeRepository;
    private final AcquireRepository acquireRepository;

    private final NotificationService notificationService;

    public void acquireBadge(User user, BadgeInfo badgeInfo) {
        if (!BadgeServiceUtils.hasBadge(badgeRepository, acquireRepository, badgeInfo, user.getOnboarding())) {
            Acquire acquire = acquireRepository.save(Acquire.newInstance(user.getOnboarding(), badgeRepository.findBadgeByBadgeInfo(badgeInfo)));
            user.getOnboarding().addAcquire(acquire);
            notificationService.sendNewBadgeNotification(user, badgeInfo);
        }
    }
}
