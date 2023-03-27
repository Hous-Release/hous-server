package hous.server.domain.notification.mongo;

import hous.server.domain.notification.Notification;
import hous.server.domain.user.Onboarding;

import java.util.List;

public interface NotificationRepositoryCustom {

    List<Notification> findNotificationsByOnboardingAndCursor(Onboarding onboarding, Long lastNotificationId, int size);

    long countAllByOnboarding(Onboarding onboarding);

    void deleteAllByOnboarding(Onboarding onboarding);
}
