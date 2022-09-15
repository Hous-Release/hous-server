package hous.server.service.notification;

import hous.server.domain.badge.BadgeInfo;
import hous.server.domain.notification.Notification;
import hous.server.domain.notification.NotificationMessage;
import hous.server.domain.notification.NotificationType;
import hous.server.domain.notification.PushMessage;
import hous.server.domain.notification.repository.NotificationRepository;
import hous.server.domain.user.Onboarding;
import hous.server.domain.user.User;
import hous.server.service.firebase.FirebaseCloudMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final FirebaseCloudMessageService firebaseCloudMessageService;

    public void sendNewBadgeNotification(User to, BadgeInfo badgeInfo) {
        notificationRepository.save(Notification.newInstance(to.getOnboarding(), NotificationType.BADGE, newBadgeNotification(badgeInfo), false));
        firebaseCloudMessageService.sendMessageTo(to.getFcmToken(), newBadgePushTitle(badgeInfo), newBadgePushBody(to.getOnboarding()));
    }

    private String newBadgePushTitle(BadgeInfo badgeInfo) {
        return String.format("'%s' %s", badgeInfo.getValue(), PushMessage.NEW_BADGE.getTitle());
    }

    private String newBadgePushBody(Onboarding onboarding) {
        return String.format("%s%s", onboarding.getNickname(), PushMessage.NEW_BADGE.getBody());
    }

    private String newBadgeNotification(BadgeInfo badgeInfo) {
        return String.format("'%s' %s", badgeInfo.getValue(), NotificationMessage.NEW_BADGE.getValue());
    }
}
