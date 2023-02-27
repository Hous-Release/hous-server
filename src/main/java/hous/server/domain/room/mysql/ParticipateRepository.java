package hous.server.domain.room.mysql;

import hous.server.domain.room.Participate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipateRepository extends JpaRepository<Participate, Long>, ParticipateRepositoryCustom {
}
