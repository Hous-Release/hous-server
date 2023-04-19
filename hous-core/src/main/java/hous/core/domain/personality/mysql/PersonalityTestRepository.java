package hous.core.domain.personality.mysql;

import hous.core.domain.personality.PersonalityTest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonalityTestRepository extends JpaRepository<PersonalityTest, Long>, PersonalityTestRepositoryCustom {
}
