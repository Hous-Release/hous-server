package hous.server.service.todo.dto.response;

import lombok.*;

import java.util.List;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class TodoAllMemberResponse {

    private int totalRoomTodoCnt;
    private List<TodoAllMemberInfo> todos;

    public static TodoAllMemberResponse of(int totalRoomTodoCnt, List<TodoAllMemberInfo> todos) {
        return TodoAllMemberResponse.builder()
                .totalRoomTodoCnt(totalRoomTodoCnt)
                .todos(todos)
                .build();
    }
}
