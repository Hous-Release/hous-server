package hous.server.domain.personality.mysql;

import hous.server.domain.personality.PersonalityTest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonalityTestRepository extends JpaRepository<PersonalityTest, Long>, PersonalityTestRepositoryCustom {
}
