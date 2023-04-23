package hous.api.service.todo.dto.response;

import hous.api.service.user.UserServiceUtils;
import hous.core.domain.todo.OurTodoStatus;
import hous.core.domain.user.Onboarding;
import lombok.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class OurTodoInfo {

    private String todoName;
    List<String> nicknames;
    private OurTodoStatus status;

    public static OurTodoInfo of(String todoName, OurTodoStatus status, Set<Onboarding> onboardings, Onboarding me) {
        List<Onboarding> sortByTestScore = onboardings.stream().sorted(Onboarding::compareTo).collect(Collectors.toList());
        List<Onboarding> meFirstList = UserServiceUtils.toMeFirstList(sortByTestScore, me);
        return OurTodoInfo.builder()
                .todoName(todoName)
                .status(status)
                .nicknames(meFirstList.stream()
                        .map(Onboarding::getNickname)
                        .collect(Collectors.toList()))
                .build();
    }
}
