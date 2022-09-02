package hous.server.service.todo.dto.response;

import hous.server.domain.todo.DayOfWeek;
import hous.server.domain.todo.Todo;
import hous.server.domain.user.Onboarding;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class TodoSummaryInfoResponse {

    private String name;
    private List<UserPersonalityInfo> selectedUsers;
    private String dayOfWeeks;

    public static TodoSummaryInfoResponse of(Todo todo, List<UserPersonalityInfo> userPersonalityInfos, Onboarding me) {
        Set<DayOfWeek> dayOfWeekSet = new HashSet<>();
        todo.getTakes().forEach(take -> take.getRedos().forEach(redo -> dayOfWeekSet.add(redo.getDayOfWeek())));
        return TodoSummaryInfoResponse.builder()
                .name(todo.getName())
                .selectedUsers(UserPersonalityInfo.sortMeFirst(userPersonalityInfos, me))
                .dayOfWeeks(DayOfWeek.toString(dayOfWeekSet))
                .build();
    }
}
