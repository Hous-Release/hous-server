package hous.server.domain.room.repository;

import hous.server.domain.room.Room;

public interface RoomRepositoryCustom {

    boolean existsByRoomCode(String code);

    Room findRoomById(Long id);

    Room findRoomByCode(String code);
}
