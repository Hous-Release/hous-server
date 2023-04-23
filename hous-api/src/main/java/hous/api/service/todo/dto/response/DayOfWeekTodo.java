package hous.api.service.todo.dto.response;

import lombok.*;

import java.util.List;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class DayOfWeekTodo {
    private String dayOfWeek;
    private int todoCnt;
    private List<TodoInfo> dayOfWeekTodos;

    public static DayOfWeekTodo of(String dayOfWeek, int todoCnt, List<TodoInfo> dayOfWeekTodos) {
        return DayOfWeekTodo.builder()
                .dayOfWeek(dayOfWeek)
                .todoCnt(todoCnt)
                .dayOfWeekTodos(dayOfWeekTodos)
                .build();
    }
}
