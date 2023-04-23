package hous.core.domain.personality.mysql;

import hous.core.domain.personality.Personality;
import hous.core.domain.personality.PersonalityColor;

public interface PersonalityRepositoryCustom {

    Personality findPersonalityByColor(PersonalityColor color);
}
