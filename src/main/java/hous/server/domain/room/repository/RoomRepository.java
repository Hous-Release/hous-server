package hous.server.domain.room.repository;

import hous.server.domain.room.Room;
import hous.server.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long>, RoomRepositoryCustom {
}
