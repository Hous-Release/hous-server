package hous.core.domain.personality.mysql;

import org.springframework.data.jpa.repository.JpaRepository;

import hous.core.domain.personality.Personality;

public interface PersonalityRepository extends JpaRepository<Personality, Long>, PersonalityRepositoryCustom {
}
