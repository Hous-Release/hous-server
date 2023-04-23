package hous.api.service.notification;

import hous.api.service.notification.dto.response.NotificationsInfoResponse;
import hous.api.service.user.UserServiceUtils;
import hous.core.domain.common.collection.ScrollPaginationCollection;
import hous.core.domain.notification.Notification;
import hous.core.domain.notification.mongo.NotificationRepository;
import hous.core.domain.user.Onboarding;
import hous.core.domain.user.User;
import hous.core.domain.user.mysql.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class NotificationRetrieveService {

    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;

    @Transactional
    public NotificationsInfoResponse getNotificationsInfo(int size, Long lastNotificationId, Long userId) {
        User user = UserServiceUtils.findUserById(userRepository, userId);
        Onboarding me = user.getOnboarding();
        List<Notification> notifications = notificationRepository.findNotificationsByOnboardingAndCursor(me, lastNotificationId, size + 1);
        ScrollPaginationCollection<Notification> notificationsCursor = ScrollPaginationCollection.of(notifications, size);
        NotificationsInfoResponse response = NotificationsInfoResponse.of(notificationsCursor, notificationRepository.countAllByOnboarding(me));
        List<Notification> readNotifications = notificationsCursor.getCurrentScrollItems();
        readNotifications.stream()
                .filter(notification -> !notification.isRead())
                .forEach(notification -> {
                    notification.updateIsRead();
                    notificationRepository.save(notification);
                });
        return response;
    }
}
