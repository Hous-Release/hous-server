package hous.server.domain.todo.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TakeRepositoryImpl implements TakeRepositoryCustom {

    private final JPAQueryFactory queryFactory;
}
