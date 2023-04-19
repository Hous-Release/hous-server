package hous.api.service.todo.dto.response;

import hous.core.domain.todo.DayOfWeek;
import hous.core.domain.todo.Redo;
import hous.core.domain.todo.Todo;
import hous.core.domain.user.Onboarding;
import lombok.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class MyTodoInfoResponse {

    private int myTodosCnt;
    private List<TodoWithDayOfWeek> myTodos;

    @ToString
    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder(access = AccessLevel.PRIVATE)
    private static class TodoWithDayOfWeek {
        private String todoName;
        private String dayOfWeeks;
    }

    public static MyTodoInfoResponse of(List<Todo> myTodos, Onboarding me) {
        return MyTodoInfoResponse.builder()
                .myTodosCnt(myTodos.size())
                .myTodos(myTodos.stream()
                        .map(myTodo -> TodoWithDayOfWeek.builder()
                                .todoName(myTodo.getName())
                                .dayOfWeeks(DayOfWeek.toString(myTodo.getTakes().stream()
                                        .filter(take -> take.getOnboarding().getId().equals(me.getId()))
                                        .findFirst().get().getRedos().stream()
                                        .map(Redo::getDayOfWeek)
                                        .sorted(Comparator.comparing(DayOfWeek::getIndex))
                                        .collect(Collectors.toSet())))
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}
