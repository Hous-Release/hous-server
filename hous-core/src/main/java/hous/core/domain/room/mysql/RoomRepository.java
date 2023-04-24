package hous.core.domain.room.mysql;

import org.springframework.data.jpa.repository.JpaRepository;

import hous.core.domain.room.Room;

public interface RoomRepository extends JpaRepository<Room, Long>, RoomRepositoryCustom {
}
