package hous.server.service.todo.dto.response;

import lombok.*;

import java.util.List;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class TodoAllDayResponse {

    private String dayOfWeek;
    private int ourTodosCnt;
    private List<TodoInfo> todoInfos;
    private List<OurTodo> ourTodos;

    public static TodoAllDayResponse of(String dayOfWeek, List<TodoInfo> todoInfos, List<OurTodo> ourTodos) {
        return TodoAllDayResponse.builder()
                .dayOfWeek(dayOfWeek)
                .ourTodosCnt(ourTodos.size())
                .todoInfos(todoInfos)
                .ourTodos(ourTodos)
                .build();
    }
}
