package hous.server.service.todo.dto.response;

import hous.server.domain.todo.OurTodoStatus;
import hous.server.domain.user.Onboarding;
import hous.server.service.user.UserServiceUtils;
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
