package hous.core.domain.notification.mongo;

import hous.core.domain.notification.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotificationRepository extends MongoRepository<Notification, Long>, NotificationRepositoryCustom {
}
