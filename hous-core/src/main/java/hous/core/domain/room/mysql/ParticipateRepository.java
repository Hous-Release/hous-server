package hous.core.domain.room.mysql;

import hous.core.domain.room.Participate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipateRepository extends JpaRepository<Participate, Long>, ParticipateRepositoryCustom {
}
