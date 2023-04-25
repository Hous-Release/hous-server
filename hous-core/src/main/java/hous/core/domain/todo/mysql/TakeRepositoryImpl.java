package hous.core.domain.todo.mysql;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TakeRepositoryImpl implements TakeRepositoryCustom {

	private final JPAQueryFactory queryFactory;
}
