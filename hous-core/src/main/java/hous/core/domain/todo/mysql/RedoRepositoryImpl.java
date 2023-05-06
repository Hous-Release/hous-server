package hous.core.domain.todo.mysql;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RedoRepositoryImpl implements RedoRepositoryCustom {

	private final JPAQueryFactory queryFactory;
}
