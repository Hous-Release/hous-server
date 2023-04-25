package hous.core.domain.personality.mysql;

import org.springframework.data.jpa.repository.JpaRepository;

import hous.core.domain.personality.PersonalityTest;

public interface PersonalityTestRepository
	extends JpaRepository<PersonalityTest, Long>, PersonalityTestRepositoryCustom {
}
