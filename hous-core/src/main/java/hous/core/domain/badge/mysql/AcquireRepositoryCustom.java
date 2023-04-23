package hous.core.domain.badge.mysql;

import hous.core.domain.badge.Acquire;
import hous.core.domain.badge.Badge;
import hous.core.domain.user.Onboarding;

import java.util.List;

public interface AcquireRepositoryCustom {

    boolean existsByOnboardingAndBadge(Onboarding onboarding, Badge badge);

    List<Acquire> findAllAcquireByOnboarding(Onboarding onboarding);
}
