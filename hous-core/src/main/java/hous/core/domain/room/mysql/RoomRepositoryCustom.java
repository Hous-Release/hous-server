package hous.core.domain.room.mysql;

import hous.core.domain.room.Room;

public interface RoomRepositoryCustom {

    boolean existsByRoomCode(String code);

    Room findRoomById(Long id);

    Room findRoomByCode(String code);

}
