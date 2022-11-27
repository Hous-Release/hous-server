package hous.server.service.todo.dto.response;

import hous.server.domain.todo.OurTodoStatus;
import hous.server.domain.user.Onboarding;
import lombok.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    public static OurTodoInfo of(String todoName, OurTodoStatus status, Set<Onboarding> onboardings) {
        return OurTodoInfo.builder()
                .todoName(todoName)
                .status(status)
                .nicknames(onboardings.stream()
                        .sorted(Onboarding::compareTo)
                        .map(Onboarding::getNickname)
                        .collect(Collectors.toList()))
                .build();
    }
}
