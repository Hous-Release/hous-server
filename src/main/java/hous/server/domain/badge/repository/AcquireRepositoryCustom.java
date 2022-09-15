package hous.server.domain.badge.repository;

import hous.server.domain.badge.Acquire;
import hous.server.domain.badge.Badge;
import hous.server.domain.user.Onboarding;

import java.util.List;

public interface AcquireRepositoryCustom {

    boolean existsByOnboardingAndBadge(Onboarding onboarding, Badge badge);

    List<Acquire> findAllAcquireByOnboarding(Onboarding onboarding);
}
