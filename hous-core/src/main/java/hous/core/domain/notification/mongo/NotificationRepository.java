package hous.core.domain.notification.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

import hous.core.domain.notification.Notification;

public interface NotificationRepository extends MongoRepository<Notification, Long>, NotificationRepositoryCustom {
}
