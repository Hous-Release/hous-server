package hous.server.service.todo;

import hous.server.common.exception.ForbiddenException;
import hous.server.domain.room.Room;
import hous.server.domain.todo.repository.TodoRepository;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static hous.server.common.exception.ErrorCode.FORBIDDEN_TODO_COUNT_EXCEPTION;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TodoServiceUtils {

    public static void validateTodoCounts(TodoRepository todoRepository, Room room) {
        if (todoRepository.findCountsByRoom(room) >= 60) {
            throw new ForbiddenException(String.format("방 (%s) 의 todo 는 60개를 초과할 수 없습니다.", room.getId()), FORBIDDEN_TODO_COUNT_EXCEPTION);
        }
    }
}
