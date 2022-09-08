package hous.server.service.todo.dto.response;

import hous.server.domain.todo.OurTodoStatus;
import lombok.*;

import java.util.Set;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OurTodoInfo extends OurTodo {

    private OurTodoStatus status;

    @Builder(access = AccessLevel.PRIVATE)
    public OurTodoInfo(String todoName, Set<String> nicknames, OurTodoStatus status) {
        super(todoName, nicknames);
        this.status = status;
    }

    public static OurTodoInfo of(String todoName, OurTodoStatus status, Set<String> nicknames) {
        return OurTodoInfo.builder()
                .todoName(todoName)
                .status(status)
                .nicknames(nicknames)
                .build();
    }
}
