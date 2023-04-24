package hous.core.domain.badge.mysql;

import java.util.List;

import hous.core.domain.badge.Badge;
import hous.core.domain.badge.BadgeInfo;

public interface BadgeRepositoryCustom {

	Badge findBadgeByBadgeInfo(BadgeInfo badgeInfo);

	List<Badge> findAllBadge();

	Badge findBadgeByBadgeId(Long id);
}
