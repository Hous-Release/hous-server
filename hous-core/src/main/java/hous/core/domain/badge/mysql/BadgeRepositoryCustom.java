package hous.core.domain.badge.mysql;

import hous.core.domain.badge.Badge;
import hous.core.domain.badge.BadgeInfo;

import java.util.List;

public interface BadgeRepositoryCustom {

    Badge findBadgeByBadgeInfo(BadgeInfo badgeInfo);

    List<Badge> findAllBadge();

    Badge findBadgeByBadgeId(Long id);
}
