package hous.server.domain.user.repository;

import hous.server.domain.user.Onboarding;

public interface OnboardingRepositoryCustom {

    Onboarding findOnboardingById(Long id);
}
