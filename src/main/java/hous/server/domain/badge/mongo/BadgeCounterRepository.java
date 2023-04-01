package hous.server.domain.badge.mongo;

import hous.server.domain.badge.BadgeCounter;
import hous.server.domain.badge.BadgeCounterType;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BadgeCounterRepository extends MongoRepository<BadgeCounter, Long> {
    BadgeCounter findByUserIdAndCountType(Long userId, BadgeCounterType countType);

    void deleteBadgeCounterByUserIdAndCountType(Long userId, BadgeCounterType countType);
}
