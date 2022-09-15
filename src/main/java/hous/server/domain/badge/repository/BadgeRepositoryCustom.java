package hous.server.domain.badge.repository;

import hous.server.domain.badge.Badge;
import hous.server.domain.badge.BadgeInfo;

import java.util.List;

public interface BadgeRepositoryCustom {

    Badge findBadgeByBadgeInfo(BadgeInfo badgeInfo);

    List<Badge> findAllBadge();

    Badge findBadgeByBadgeId(Long id);
}
