package hous.server.domain.room.repository;

import hous.server.domain.room.Participate;
import hous.server.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipateRepository extends JpaRepository<Participate, Long>, ParticipateRepositoryCustom {
}
