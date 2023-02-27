package hous.server.domain.room.mysql;

import hous.server.domain.user.Onboarding;

public interface ParticipateRepositoryCustom {

    boolean existsByOnboarding(Onboarding onboarding);
}
