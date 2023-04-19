package hous.core.domain.personality.mysql;

import hous.core.domain.personality.Personality;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonalityRepository extends JpaRepository<Personality, Long>, PersonalityRepositoryCustom {
}
