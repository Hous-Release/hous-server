package hous.server.service.todo.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class MyTodoInfo {

    private Long todoId;
    private String todoName;
    private boolean isChecked;

    @JsonProperty("isChecked")
    public boolean isChecked() {
        return isChecked;
    }

    public static MyTodoInfo of(Long todoId, String todoName, boolean isChecked) {
        return MyTodoInfo.builder()
                .todoId(todoId)
                .todoName(todoName)
                .isChecked(isChecked)
                .build();
    }
}
