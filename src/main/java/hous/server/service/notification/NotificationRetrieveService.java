package hous.server.service.notification;

import hous.server.domain.common.collection.ScrollPaginationCollection;
import hous.server.domain.notification.Notification;
import hous.server.domain.notification.mongo.NotificationRepository;
import hous.server.domain.user.Onboarding;
import hous.server.domain.user.User;
import hous.server.domain.user.mysql.UserRepository;
import hous.server.service.notification.dto.response.NotificationsInfoResponse;
import hous.server.service.user.UserServiceUtils;
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
        return NotificationsInfoResponse.of(notificationsCursor, notificationRepository.countAllByOnboarding(me));
    }
}
