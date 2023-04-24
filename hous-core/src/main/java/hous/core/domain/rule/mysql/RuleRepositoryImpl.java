package hous.core.domain.rule.mysql;

import static hous.core.domain.rule.QRule.*;

import com.querydsl.jpa.impl.JPAQueryFactory;

import hous.core.domain.room.Room;
import hous.core.domain.rule.Rule;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RuleRepositoryImpl implements RuleRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public Rule findLastRuleByRoom(Room room) {
		return queryFactory
			.selectFrom(rule)
			.where(rule.room.eq(room))
			.orderBy(rule.idx.desc())
			.fetchFirst();
	}

	@Override
	public Rule findRuleByIdAndRoom(Long ruleId, Room room) {
		return queryFactory
			.selectFrom(rule)
			.where(rule.id.eq(ruleId), rule.room.eq(room))
			.fetchOne();
	}
}
