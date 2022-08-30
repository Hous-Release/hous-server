package hous.server.domain.rule.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import hous.server.domain.room.Room;
import hous.server.domain.rule.Rule;
import lombok.RequiredArgsConstructor;

import static hous.server.domain.rule.QRule.rule;


@RequiredArgsConstructor
public class RuleRepositoryImpl implements RuleRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Rule findLastRuleByRoomId(Room room) {
        return queryFactory
                .selectFrom(rule)
                .where(rule.room.eq(room))
                .orderBy(rule.idx.desc())
                .fetchFirst();
    }
}
