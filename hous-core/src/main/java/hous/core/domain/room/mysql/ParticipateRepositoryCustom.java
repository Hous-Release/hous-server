package hous.core.domain.room.mysql;

import hous.core.domain.user.Onboarding;

public interface ParticipateRepositoryCustom {

	boolean existsByOnboarding(Onboarding onboarding);
}
