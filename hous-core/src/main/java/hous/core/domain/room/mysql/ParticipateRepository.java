package hous.core.domain.room.mysql;

import org.springframework.data.jpa.repository.JpaRepository;

import hous.core.domain.room.Participate;

public interface ParticipateRepository extends JpaRepository<Participate, Long>, ParticipateRepositoryCustom {
}
