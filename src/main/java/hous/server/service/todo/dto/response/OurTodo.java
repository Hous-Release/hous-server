package hous.server.service.todo.dto.response;

import hous.server.domain.user.Onboarding;
import hous.server.service.user.UserServiceUtils;
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

    private String todoName;
    private List<String> nicknames;

    public static OurTodo of(String todoName, Set<Onboarding> onboardings, Onboarding me) {
        List<Onboarding> sortByTestScore = onboardings.stream().sorted(Onboarding::compareTo).collect(Collectors.toList());
        List<Onboarding> meFirstList = UserServiceUtils.toMeFirstList(sortByTestScore, me);
        return OurTodo.builder()
                .todoName(todoName)
                .nicknames(meFirstList.stream()
                        .map(Onboarding::getNickname)
                        .collect(Collectors.toList()))
                .build();
    }
}
