package hous.core.domain.user.mysql;

import hous.core.domain.user.Onboarding;

public interface OnboardingRepositoryCustom {

    Onboarding findOnboardingById(Long id);
}
