package hous.server.domain.personality.mysql;

import hous.server.domain.personality.PersonalityTest;

import java.util.List;

public interface PersonalityTestRepositoryCustom {

    List<PersonalityTest> findAllPersonalityTest();
}
