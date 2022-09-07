package hous.server.service.todo.dto.response;

import lombok.*;

import java.util.List;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(access = AccessLevel.PRIVATE)
public class OurTodo {

    private String todoName;
    private List<String> nicknames;

    public static OurTodo of(String todoName, List<String> nicknames) {
        return OurTodo.builder()
                .todoName(todoName)
                .nicknames(nicknames)
                .build();
    }
}
