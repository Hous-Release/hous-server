package hous.core.domain.badge.mysql;

import java.util.List;

import hous.core.domain.badge.Acquire;
import hous.core.domain.badge.Badge;
import hous.core.domain.user.Onboarding;

public interface AcquireRepositoryCustom {

	boolean existsByOnboardingAndBadge(Onboarding onboarding, Badge badge);

	List<Acquire> findAllAcquireByOnboarding(Onboarding onboarding);
}
