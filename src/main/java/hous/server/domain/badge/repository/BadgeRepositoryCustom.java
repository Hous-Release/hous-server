package hous.server.domain.badge.repository;

import hous.server.domain.badge.Badge;
import hous.server.domain.badge.BadgeInfo;

public interface BadgeRepositoryCustom {

    Badge findBadgeByBadgeInfo(BadgeInfo badgeInfo);
}
