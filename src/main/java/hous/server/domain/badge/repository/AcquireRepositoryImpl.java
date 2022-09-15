package hous.server.domain.badge.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AcquireRepositoryImpl implements AcquireRepositoryCustom {

    private final JPAQueryFactory queryFactory;
}
