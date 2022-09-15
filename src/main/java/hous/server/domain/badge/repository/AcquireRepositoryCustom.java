package hous.server.domain.badge.repository;

import hous.server.domain.badge.Badge;
import hous.server.domain.user.Onboarding;

public interface AcquireRepositoryCustom {

    boolean existsByOnboardingAndBadge(Onboarding onboarding, Badge badge);
}
