package hous.core.domain.badge.mysql;

import com.querydsl.jpa.impl.JPAQueryFactory;
import hous.core.domain.badge.Represent;
import hous.core.domain.user.Onboarding;
import lombok.RequiredArgsConstructor;

import static hous.core.domain.badge.QRepresent.represent;

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
