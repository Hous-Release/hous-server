package hous.server.service.todo.dto.response;

import lombok.*;

import java.util.List;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class TodoAllDayResponse {

    private int totalRoomTodoCnt;
    private List<TodoAllDayInfo> todos;

    public static TodoAllDayResponse of(int totalRoomTodoCnt, List<TodoAllDayInfo> todos) {
        return TodoAllDayResponse.builder()
                .totalRoomTodoCnt(totalRoomTodoCnt)
                .todos(todos)
                .build();
    }
}
