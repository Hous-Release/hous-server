package hous.server.service.todo.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TodoDetailInfo extends TodoInfo {

    private boolean isChecked;

    @JsonProperty("isChecked")
    public boolean isChecked() {
        return isChecked;
    }

    @Builder(access = AccessLevel.PRIVATE)
    public TodoDetailInfo(Long todoId, String todoName, boolean isChecked) {
        super(todoId, todoName);
        this.isChecked = isChecked;
    }

    public static TodoDetailInfo of(Long todoId, String todoName, boolean isChecked) {
        return TodoDetailInfo.builder()
                .todoId(todoId)
                .todoName(todoName)
                .isChecked(isChecked)
                .build();
    }
}
