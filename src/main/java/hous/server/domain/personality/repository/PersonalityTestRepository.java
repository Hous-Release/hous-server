package hous.server.domain.personality.repository;

import hous.server.domain.personality.PersonalityTest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonalityTestRepository extends JpaRepository<PersonalityTest, Long>, PersonalityTestRepositoryCustom {
}
