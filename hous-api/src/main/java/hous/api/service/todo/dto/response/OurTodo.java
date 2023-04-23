package hous.api.service.todo.dto.response;

import hous.api.service.user.UserServiceUtils;
import hous.core.domain.user.Onboarding;
import lombok.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(access = AccessLevel.PRIVATE)
public class OurTodo {

    private Long todoId;
    private String todoName;
    private List<String> nicknames;

    public static OurTodo of(Long todoId, String todoName, Set<Onboarding> onboardings, Onboarding me) {
        List<Onboarding> sortByTestScore = onboardings.stream().sorted(Onboarding::compareTo).collect(Collectors.toList());
        List<Onboarding> meFirstList = UserServiceUtils.toMeFirstList(sortByTestScore, me);
        return OurTodo.builder()
                .todoId(todoId)
                .todoName(todoName)
                .nicknames(meFirstList.stream()
                        .map(Onboarding::getNickname)
                        .collect(Collectors.toList()))
                .build();
    }
}
