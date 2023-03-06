package hous.server.domain.badge.mongo;

import hous.server.domain.badge.BadgeCounter;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BadgeCounterRepository extends MongoRepository<BadgeCounter, Long> {
}
