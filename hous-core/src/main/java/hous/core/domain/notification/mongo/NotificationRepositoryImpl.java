package hous.core.domain.notification.mongo;

import hous.core.domain.notification.Notification;
import hous.core.domain.user.Onboarding;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

@RequiredArgsConstructor
public class NotificationRepositoryImpl implements NotificationRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    @Override
    public List<Notification> findNotificationsByOnboardingAndCursor(Onboarding onboarding, Long lastNotificationId, int size) {
        Query query = new Query();
        query.addCriteria(Criteria.where("onboarding_id").is(onboarding.getId()).and("_id").lt(lastNotificationId));
        query.limit(size);
        query.with(Sort.by(Sort.Direction.DESC, "_id"));

        return mongoTemplate.find(query, Notification.class);
    }

    @Override
    public long countAllByOnboarding(Onboarding onboarding) {
        Query query = new Query();
        query.addCriteria(Criteria.where("onboarding_id").is(onboarding.getId()));

        return mongoTemplate.find(query, Notification.class).size();
    }

    @Override
    public void deleteAllByOnboarding(Onboarding onboarding) {
        Query query = new Query();
        query.addCriteria(Criteria.where("onboarding_id").is(onboarding.getId()));

        mongoTemplate.remove(query, Notification.class);
    }
}
