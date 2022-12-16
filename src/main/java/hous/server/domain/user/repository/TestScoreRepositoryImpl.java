package hous.server.domain.user.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TestScoreRepositoryImpl implements TestScoreRepositoryCustom {

    private final JPAQueryFactory queryFactory;
}
