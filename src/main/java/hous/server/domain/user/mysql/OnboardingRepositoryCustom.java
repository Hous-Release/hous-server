package hous.server.domain.user.mysql;

import hous.server.domain.user.Onboarding;

public interface OnboardingRepositoryCustom {

    Onboarding findOnboardingById(Long id);
}
