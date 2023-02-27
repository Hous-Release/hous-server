package hous.server.domain.room.mysql;

import hous.server.domain.room.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long>, RoomRepositoryCustom {
}
