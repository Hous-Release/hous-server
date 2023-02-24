package hous.server.domain.user.mysql;

import hous.server.domain.user.Onboarding;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OnboardingRepository extends JpaRepository<Onboarding, Long>, OnboardingRepositoryCustom {
}
