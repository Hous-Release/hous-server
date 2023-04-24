package hous.core.domain.personality.mysql;

import java.util.List;

import hous.core.domain.personality.PersonalityTest;

public interface PersonalityTestRepositoryCustom {

	List<PersonalityTest> findAllPersonalityTest();
}
