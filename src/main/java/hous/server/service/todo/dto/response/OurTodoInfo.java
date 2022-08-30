package hous.server.service.todo.dto.response;

import hous.server.domain.todo.OurTodoStatus;
import lombok.*;

import java.util.List;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class OurTodoInfo {

    private String todoName;
    private OurTodoStatus status;
    private List<String> nicknames;

    public static OurTodoInfo of(String todoName, OurTodoStatus status, List<String> nicknames) {
        return OurTodoInfo.builder()
                .todoName(todoName)
                .status(status)
                .nicknames(nicknames)
                .build();
    }
}
