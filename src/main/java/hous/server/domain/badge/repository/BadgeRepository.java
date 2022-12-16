package hous.server.domain.badge.repository;

import hous.server.domain.badge.Badge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BadgeRepository extends JpaRepository<Badge, Long>, BadgeRepositoryCustom {
}
