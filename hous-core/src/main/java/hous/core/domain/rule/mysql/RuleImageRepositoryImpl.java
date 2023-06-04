package hous.core.domain.rule.mysql;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RuleImageRepositoryImpl implements RuleImageRepositoryCustom {

	private final JPAQueryFactory queryFactory;
}
