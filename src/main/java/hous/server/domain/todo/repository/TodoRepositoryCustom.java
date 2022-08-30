package hous.server.domain.todo.repository;

import hous.server.domain.room.Room;

public interface TodoRepositoryCustom {

    int findCountsByRoom(Room room);
}
