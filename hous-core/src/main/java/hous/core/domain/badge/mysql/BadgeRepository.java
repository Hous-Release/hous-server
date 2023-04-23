package hous.core.domain.badge.mysql;

import hous.core.domain.badge.Badge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BadgeRepository extends JpaRepository<Badge, Long>, BadgeRepositoryCustom {
}
