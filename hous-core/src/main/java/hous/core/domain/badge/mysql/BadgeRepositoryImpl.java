package hous.core.domain.badge.mysql;

import com.querydsl.jpa.impl.JPAQueryFactory;
import hous.core.domain.badge.Badge;
import hous.core.domain.badge.BadgeInfo;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static hous.core.domain.badge.QBadge.badge;

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

    @Override
    public Badge findBadgeByBadgeId(Long id) {
        return queryFactory
                .selectFrom(badge)
                .where(badge.id.eq(id))
                .fetchOne();
    }
}
