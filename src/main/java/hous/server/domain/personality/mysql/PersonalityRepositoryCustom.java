package hous.server.domain.personality.mysql;

import hous.server.domain.personality.Personality;
import hous.server.domain.personality.PersonalityColor;

public interface PersonalityRepositoryCustom {

    Personality findPersonalityByColor(PersonalityColor color);
}
