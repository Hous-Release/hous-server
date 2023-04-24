package hous.core.domain.badge.mysql;

import static hous.core.domain.badge.QRepresent.*;

import com.querydsl.jpa.impl.JPAQueryFactory;

import hous.core.domain.badge.Represent;
import hous.core.domain.user.Onboarding;
import lombok.RequiredArgsConstructor;

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
