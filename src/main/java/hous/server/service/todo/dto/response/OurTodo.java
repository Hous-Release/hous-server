package hous.server.service.todo.dto.response;

import lombok.*;

import java.util.Set;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(access = AccessLevel.PRIVATE)
public class OurTodo {

    private String todoName;
    private Set<String> nicknames;

    public static OurTodo of(String todoName, Set<String> nicknames) {
        return OurTodo.builder()
                .todoName(todoName)
                .nicknames(nicknames)
                .build();
    }
}
