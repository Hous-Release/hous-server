package hous.server.domain.badge.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import hous.server.domain.badge.Badge;
import hous.server.domain.user.Onboarding;
import lombok.RequiredArgsConstructor;

import static hous.server.domain.badge.QAcquire.acquire;

@RequiredArgsConstructor
public class AcquireRepositoryImpl implements AcquireRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public boolean existsByOnboardingAndBadge(Onboarding onboarding, Badge badge) {
        return queryFactory.selectFrom(acquire)
                .where(
                        acquire.onboarding.eq(onboarding),
                        acquire.badge.eq(badge)
                ).fetchOne() != null;
    }
}
