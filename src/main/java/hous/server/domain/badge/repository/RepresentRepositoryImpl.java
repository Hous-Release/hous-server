package hous.server.domain.badge.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import hous.server.domain.badge.Represent;
import hous.server.domain.user.Onboarding;
import lombok.RequiredArgsConstructor;

import static hous.server.domain.badge.QRepresent.represent;

@RequiredArgsConstructor
public class RepresentRepositoryImpl implements RepresentRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Represent findRepresentByOnboarding(Onboarding onboarding) {
        return queryFactory
                .selectFrom(represent)
                .where(represent.onboarding.eq(onboarding))
                .fetchOne();
    }
}
