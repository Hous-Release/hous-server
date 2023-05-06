package hous.core.domain.badge.mysql;

import static hous.core.domain.badge.QAcquire.*;

import java.util.List;

import com.querydsl.jpa.impl.JPAQueryFactory;

import hous.core.domain.badge.Acquire;
import hous.core.domain.badge.Badge;
import hous.core.domain.user.Onboarding;
import lombok.RequiredArgsConstructor;

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

	@Override
	public List<Acquire> findAllAcquireByOnboarding(Onboarding onboarding) {
		return queryFactory.selectFrom(acquire)
			.where(acquire.onboarding.eq(onboarding))
			.orderBy(acquire.badge.id.asc())
			.fetch();
	}
}
