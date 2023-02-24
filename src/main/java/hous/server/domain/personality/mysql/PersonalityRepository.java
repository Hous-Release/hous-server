package hous.server.domain.personality.mysql;

import hous.server.domain.personality.Personality;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonalityRepository extends JpaRepository<Personality, Long>, PersonalityRepositoryCustom {
}
