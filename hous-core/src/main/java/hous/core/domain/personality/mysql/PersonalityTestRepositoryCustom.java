package hous.core.domain.personality.mysql;

import hous.core.domain.personality.PersonalityTest;

import java.util.List;

public interface PersonalityTestRepositoryCustom {

    List<PersonalityTest> findAllPersonalityTest();
}
