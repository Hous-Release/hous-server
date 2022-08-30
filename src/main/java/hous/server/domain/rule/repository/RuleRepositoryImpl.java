package hous.server.domain.rule.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import hous.server.domain.rule.Rule;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static hous.server.domain.room.QRoom.room;
import static hous.server.domain.rule.QRule.rule;


@RequiredArgsConstructor
public class RuleRepositoryImpl implements RuleRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Rule findRuleIdxByRoomId(Long id) {
        return queryFactory
                .selectFrom(rule)
                .where(room.id.eq(id))
                .orderBy(rule.idx.desc())
                .fetchFirst();
    }

    @Override
    public int validateRuleCountsByRoomId(Long id) {
        return queryFactory
                .selectFrom(rule)
                .where(room.id.eq(id))
                .fetch().size();
    }

    @Override
    public List<Rule> findRulesByRoomId(Long id) {
        return queryFactory
                .selectFrom(rule)
                .where(room.id.eq(id))
                .orderBy(rule.idx.asc())
                .fetch();
    }


}
