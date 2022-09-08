package hous.server.service.todo.dto.response;

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
    private List<String> dayOfWeekTodos;

    public static DayOfWeekTodo of(String dayOfWeek, int todoCnt, List<String> dayOfWeekTodos) {
        return DayOfWeekTodo.builder()
                .dayOfWeek(dayOfWeek)
                .todoCnt(todoCnt)
                .dayOfWeekTodos(dayOfWeekTodos)
                .build();
    }
}
