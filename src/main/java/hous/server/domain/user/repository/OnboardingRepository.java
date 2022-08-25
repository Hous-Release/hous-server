package hous.server.domain.user.repository;

import hous.server.domain.user.Onboarding;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OnboardingRepository extends JpaRepository<Onboarding, Long> {
}
