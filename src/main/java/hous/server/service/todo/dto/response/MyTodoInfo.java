package hous.server.service.todo.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MyTodoInfo extends MyTodo {

    private boolean isChecked;

    @JsonProperty("isChecked")
    public boolean isChecked() {
        return isChecked;
    }

    @Builder(access = AccessLevel.PRIVATE)
    public MyTodoInfo(Long todoId, String todoName, boolean isChecked) {
        super(todoId, todoName);
        this.isChecked = isChecked;
    }

    public static MyTodoInfo of(Long todoId, String todoName, boolean isChecked) {
        return MyTodoInfo.builder()
                .todoId(todoId)
                .todoName(todoName)
                .isChecked(isChecked)
                .build();
    }
}
