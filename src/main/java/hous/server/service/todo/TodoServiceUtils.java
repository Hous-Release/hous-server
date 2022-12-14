package hous.server.service.todo;

import hous.server.common.exception.ConflictException;
import hous.server.common.exception.ForbiddenException;
import hous.server.common.exception.NotFoundException;
import hous.server.common.exception.ValidationException;
import hous.server.common.util.DateUtils;
import hous.server.domain.common.Constraint;
import hous.server.domain.room.Room;
import hous.server.domain.todo.DayOfWeek;
import hous.server.domain.todo.Done;
import hous.server.domain.todo.Todo;
import hous.server.domain.todo.repository.DoneRepository;
import hous.server.domain.todo.repository.TodoRepository;
import hous.server.domain.user.Onboarding;
import hous.server.service.todo.dto.response.UserPersonalityInfo;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static hous.server.common.exception.ErrorCode.*;

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
        if (room.getTodosCnt() >= Constraint.TODO_COUNT_MAX) {
            throw new ForbiddenException(String.format("방 (%s) 의 todo 는 60개를 초과할 수 없습니다.", room.getId()), FORBIDDEN_TODO_COUNT_EXCEPTION);
        }
    }

    public static void validateTodoStatus(DoneRepository doneRepository, boolean status, Onboarding onboarding, Todo todo) {
        if (status == doneRepository.findTodayTodoCheckStatus(DateUtils.todayLocalDate(), onboarding, todo)) {
            throw new ValidationException(String.format("(%s) 유저의 todo (%s) 상태는 이미 (%s) 입니다.", onboarding.getId(), todo.getId(), status), VALIDATION_STATUS_EXCEPTION);
        }
    }

    public static void existsTodoByRoomTodos(Room room, String requestTodo, Todo todo) {
        List<String> todos = room.getTodos().stream().map(Todo::getName).collect(Collectors.toList());
        if (todo != null) {
            todos.remove(todo.getName());
        }
        if (todos.contains(requestTodo)) {
            throw new ConflictException(String.format("방 (%s) 에 이미 존재하는 todo (%s) 입니다.", room.getId(), requestTodo), CONFLICT_TODO_EXCEPTION);
        }
    }

    public static List<UserPersonalityInfo> toUserPersonalityInfoList(List<Onboarding> onboardings) {
        return onboardings.stream()
                .map(onboarding -> UserPersonalityInfo.of(
                        onboarding.getId(),
                        onboarding.getPersonality().getColor(),
                        onboarding.getNickname()))
                .collect(Collectors.toList());
    }

    public static List<Todo> filterDayOurTodos(LocalDate day, List<Todo> todos) {
        Set<Todo> dayOurTodosSet = new HashSet<>();
        todos.forEach(todo -> {
            todo.getTakes().forEach(take -> {
                take.getRedos().forEach(redo -> {
                    if (redo.getDayOfWeek().toString().equals(DateUtils.nowDayOfWeek(day))) {
                        dayOurTodosSet.add(todo);
                    }
                });
            });
        });
        return new ArrayList<>(dayOurTodosSet);
    }

    public static List<Todo> filterDayOurTodosByIsPushNotification(LocalDate day, List<Todo> todos) {
        Set<Todo> dayOurTodosSet = new HashSet<>();
        todos.stream().filter(Todo::isPushNotification).forEach(todo ->
                todo.getTakes().forEach(take ->
                        take.getRedos().forEach(redo -> {
                            if (redo.getDayOfWeek().toString().equals(DateUtils.nowDayOfWeek(day))) {
                                dayOurTodosSet.add(todo);
                            }
                        })
                )
        );
        return new ArrayList<>(dayOurTodosSet);
    }

    public static List<Todo> filterAllDaysUserTodos(List<Todo> todos, Onboarding onboarding) {
        Set<Todo> userTodosSet = new HashSet<>();
        todos.forEach(todo -> todo.getTakes().forEach(take -> {
            if (take.getOnboarding().getId().equals(onboarding.getId())) {
                userTodosSet.add(todo);
            }
        }));
        return new ArrayList<>(userTodosSet);
    }

    public static List<Done> filterAllDaysMyDones(Onboarding me, List<Done> dones) {
        return dones.stream()
                .filter(done -> done.getOnboarding().getId().equals(me.getId()))
                .collect(Collectors.toList());
    }

    public static Map<Integer, Set<Todo>> mapByDayOfWeekToOurTodosList(List<Todo> todos) {
        Map<Integer, Set<Todo>> todosMapByDayOfWeek = new HashMap<>();
        for (int i = DayOfWeek.MONDAY.getIndex(); i <= DayOfWeek.SUNDAY.getIndex(); i++) {
            todosMapByDayOfWeek.put(i, new HashSet<>());
        }
        todos.forEach(todo -> todo.getTakes().forEach(take ->
                take.getRedos().forEach(redo ->
                {
                    Set<Todo> todosByDayOfWeek = todosMapByDayOfWeek.get(redo.getDayOfWeek().getIndex());
                    todosByDayOfWeek.add(todo);
                    todosMapByDayOfWeek.put(redo.getDayOfWeek().getIndex(), todosByDayOfWeek);
                })
        ));
        return todosMapByDayOfWeek;
    }

    public static Map<Integer, Set<Todo>> mapByDayOfWeekToMyTodosList(Onboarding me, List<Todo> todos) {
        Map<Integer, Set<Todo>> todosMapByDayOfWeek = new HashMap<>();
        for (int i = DayOfWeek.MONDAY.getIndex(); i <= DayOfWeek.SUNDAY.getIndex(); i++) {
            todosMapByDayOfWeek.put(i, new HashSet<>());
        }
        todos.forEach(todo -> todo.getTakes().forEach(take -> {
                    if (take.getOnboarding().getId().equals(me.getId())) {
                        take.getRedos().forEach(redo -> {
                            Set<Todo> todosByDayOfWeek = todosMapByDayOfWeek.get(redo.getDayOfWeek().getIndex());
                            todosByDayOfWeek.add(todo);
                            todosMapByDayOfWeek.put(redo.getDayOfWeek().getIndex(), todosByDayOfWeek);
                        });
                    }
                }
        ));
        return todosMapByDayOfWeek;
    }

    public static List<Todo> filterDayMyTodos(LocalDate day, Onboarding me, List<Todo> todos) {
        Set<Todo> dayMyTodosSet = new HashSet<>();
        List<Todo> dayOurTodosList = filterDayOurTodos(day, todos);
        // TODO refatoring 필요
        dayOurTodosList.forEach(todo -> todo.getTakes().forEach(take -> {
            if (take.getOnboarding().getId().equals(me.getId())) {
                take.getRedos().forEach(redo -> {
                    if (redo.getDayOfWeek().toString().equals(DateUtils.nowDayOfWeek(day))) {
                        dayMyTodosSet.add(todo);
                    }
                });
            }
        }));
        return new ArrayList<>(dayMyTodosSet);
    }

    public static List<Todo> filterDayMyTodosByIsPushNotification(LocalDate day, Onboarding me, List<Todo> todos) {
        Set<Todo> dayMyTodosSet = new HashSet<>();
        List<Todo> dayOurTodosList = filterDayOurTodosByIsPushNotification(day, todos);
        dayOurTodosList.forEach(todo -> todo.getTakes().forEach(take -> {
            if (take.getOnboarding().getId().equals(me.getId())) {
                dayMyTodosSet.add(todo);
            }
        }));
        return new ArrayList<>(dayMyTodosSet);
    }
}
