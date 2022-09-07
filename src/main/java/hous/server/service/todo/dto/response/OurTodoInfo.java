package hous.server.service.todo.dto.response;

import hous.server.domain.todo.OurTodoStatus;
import lombok.*;

import java.util.List;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OurTodoInfo extends OurTodo {

    private OurTodoStatus status;

    @Builder(access = AccessLevel.PRIVATE)
    public OurTodoInfo(String todoName, List<String> nicknames, OurTodoStatus status) {
        super(todoName, nicknames);
        this.status = status;
    }

    public static OurTodoInfo of(String todoName, OurTodoStatus status, List<String> nicknames) {
        return OurTodoInfo.builder()
                .todoName(todoName)
                .status(status)
                .nicknames(nicknames)
                .build();
    }
}
