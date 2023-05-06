package hous.core.domain.user.mysql;

import org.springframework.data.jpa.repository.JpaRepository;

import hous.core.domain.user.Onboarding;

public interface OnboardingRepository extends JpaRepository<Onboarding, Long>, OnboardingRepositoryCustom {
}
