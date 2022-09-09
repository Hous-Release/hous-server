package hous.server.domain.personality.repository;

import hous.server.domain.personality.PersonalityTest;

import java.util.List;

public interface PersonalityTestRepositoryCustom {

    List<PersonalityTest> findAllPersonalityTest();
}
