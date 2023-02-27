package hous.server.domain.user.mysql;

import com.querydsl.jpa.impl.JPAQueryFactory;
import hous.server.domain.user.Onboarding;
import lombok.RequiredArgsConstructor;

import static hous.server.domain.user.QOnboarding.onboarding;

@RequiredArgsConstructor
public class OnboardingRepositoryImpl implements OnboardingRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Onboarding findOnboardingById(Long id) {
        return queryFactory
                .selectFrom(onboarding)
                .where(onboarding.id.eq(id))
                .fetchOne();
    }
}
