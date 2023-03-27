package hous.server.domain.notification.mongo;

import hous.server.domain.notification.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotificationRepository extends MongoRepository<Notification, Long>, NotificationRepositoryCustom {
}
