package hous.core.domain.room.mysql;

import hous.core.domain.room.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long>, RoomRepositoryCustom {
}
