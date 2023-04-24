package hous.core.domain.notification.mongo;

import java.util.List;

import hous.core.domain.notification.Notification;
import hous.core.domain.user.Onboarding;

public interface NotificationRepositoryCustom {

	List<Notification> findNotificationsByOnboardingAndCursor(Onboarding onboarding, Long lastNotificationId, int size);

	long countAllByOnboarding(Onboarding onboarding);

	void deleteAllByOnboarding(Onboarding onboarding);
}
