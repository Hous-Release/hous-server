package hous.core.domain.badge.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

import hous.core.domain.badge.BadgeCounter;
import hous.core.domain.badge.BadgeCounterType;

public interface BadgeCounterRepository extends MongoRepository<BadgeCounter, Long> {
	BadgeCounter findByUserIdAndCountType(Long userId, BadgeCounterType countType);

	void deleteBadgeCounterByUserIdAndCountType(Long userId, BadgeCounterType countType);
}
