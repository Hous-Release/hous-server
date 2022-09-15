package hous.server.domain.badge.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import hous.server.domain.badge.Badge;
import hous.server.domain.badge.BadgeInfo;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static hous.server.domain.badge.QBadge.badge;

@RequiredArgsConstructor
public class BadgeRepositoryImpl implements BadgeRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Badge findBadgeByBadgeInfo(BadgeInfo badgeInfo) {
        return queryFactory
                .selectFrom(badge)
                .where(badge.info.eq(badgeInfo))
                .fetchOne();
    }

    @Override
    public List<Badge> findAllBadge() {
        return queryFactory
                .selectFrom(badge)
                .orderBy(badge.id.asc())
                .fetch();
    }
}
