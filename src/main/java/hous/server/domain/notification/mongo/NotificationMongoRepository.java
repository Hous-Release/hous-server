package hous.server.domain.notification.mongo;

import hous.server.domain.notification.NotificationMongo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotificationMongoRepository extends MongoRepository<NotificationMongo, Long> {
}
