package hous.server.service.todo;

import hous.server.common.exception.ForbiddenException;
import hous.server.common.exception.NotFoundException;
import hous.server.common.util.DateUtils;
import hous.server.domain.room.Room;
import hous.server.domain.todo.Todo;
import hous.server.domain.todo.repository.TodoRepository;
import hous.server.domain.user.Onboarding;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static hous.server.common.exception.ErrorCode.FORBIDDEN_TODO_COUNT_EXCEPTION;
import static hous.server.common.exception.ErrorCode.NOT_FOUND_TODO_EXCEPTION;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TodoServiceUtils {

    public static Todo findTodoById(TodoRepository todoRepository, Long todoId) {
        Todo todo = todoRepository.findTodoById(todoId);
        if (todo == null) {
            throw new NotFoundException(String.format("존재하지 않는 todo (%s) 입니다", todoId), NOT_FOUND_TODO_EXCEPTION);
        }
        return todo;
    }

    public static void validateTodoCounts(Room room) {
        if (room.getTodosCnt() >= 60) {
            throw new ForbiddenException(String.format("방 (%s) 의 todo 는 60개를 초과할 수 없습니다.", room.getId()), FORBIDDEN_TODO_COUNT_EXCEPTION);
        }
    }

    public static List<Todo> filterTodayOurTodos(LocalDate now, List<Todo> todos) {
        List<Todo> todayOurTodosList = new ArrayList<>();
        todos.forEach(todo -> {
            todo.getTakes().forEach(take -> {
                take.getRedos().forEach(redo -> {
                    if (redo.getDayOfWeek().toString().equals(DateUtils.nowDayOfWeek(now))) {
                        todayOurTodosList.add(todo);
                    }
                });
            });
        });
        return todayOurTodosList;
    }

    public static List<Todo> filterTodayMyTodos(LocalDate now, Onboarding me, List<Todo> todos) {
        List<Todo> todayMyTodosList = new ArrayList<>();
        List<Todo> todayOurTodosList = filterTodayOurTodos(now, todos);
        todayOurTodosList.forEach(todo -> {
            todo.getTakes().forEach(take -> {
                if (take.getOnboarding().getId().equals(me.getId())) {
                    todayMyTodosList.add(todo);
                }
            });
        });
        return todayMyTodosList;
    }
}
