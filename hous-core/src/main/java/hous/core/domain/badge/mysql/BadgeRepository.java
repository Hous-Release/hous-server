package hous.core.domain.badge.mysql;

import org.springframework.data.jpa.repository.JpaRepository;

import hous.core.domain.badge.Badge;

public interface BadgeRepository extends JpaRepository<Badge, Long>, BadgeRepositoryCustom {
}
