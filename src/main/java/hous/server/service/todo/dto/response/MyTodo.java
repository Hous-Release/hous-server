package hous.server.service.todo.dto.response;

import lombok.*;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(access = AccessLevel.PRIVATE)
public class MyTodo {
    private Long todoId;
    private String todoName;

    public static MyTodo of(Long todoId, String todoName) {
        return MyTodo.builder()
                .todoId(todoId)
                .todoName(todoName)
                .build();
    }
}
