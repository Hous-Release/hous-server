package hous.server.domain.room.repository;

import hous.server.domain.room.Room;
import hous.server.domain.user.Onboarding;

public interface ParticipateRepositoryCustom {

    boolean existsByOnboarding(Onboarding onboarding);

    int findCountsByRoom(Room room);
}
