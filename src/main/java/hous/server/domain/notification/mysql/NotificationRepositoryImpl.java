package hous.server.domain.notification.mysql;

import com.querydsl.jpa.impl.JPAQueryFactory;
import hous.server.domain.notification.Notification;
import hous.server.domain.user.Onboarding;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static hous.server.domain.notification.QNotification.notification;


@RequiredArgsConstructor
public class NotificationRepositoryImpl implements NotificationRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Notification> findNotificationsByOnboardingAndCursor(Onboarding onboarding, Long lastNotificationId, int size) {
        return queryFactory.selectFrom(notification)
                .where(
                        notification.onboarding.eq(onboarding),
                        notification.id.lt(lastNotificationId)
                )
                .limit(size)
                .orderBy(notification.id.desc())
                .fetch();
    }

    @Override
    public long countAllByOnboarding(Onboarding onboarding) {
        return queryFactory.selectFrom(notification)
                .where(notification.onboarding.eq(onboarding))
                .fetch().size();
    }
}
