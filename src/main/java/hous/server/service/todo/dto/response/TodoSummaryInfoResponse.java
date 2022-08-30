package hous.server.service.todo.dto.response;

import hous.server.domain.personality.PersonalityColor;
import hous.server.domain.todo.DayOfWeek;
import hous.server.domain.todo.Todo;
import hous.server.domain.user.Onboarding;
import lombok.*;

import java.util.*;
import java.util.stream.Collectors;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class TodoSummaryInfoResponse {

    private String name;
    private List<UserInfo> selectedUsers;
    private List<DayOfWeek> dayOfWeeks;

    @ToString
    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder(access = AccessLevel.PRIVATE)
    private static class UserInfo {
        private Long onboardingId;
        private PersonalityColor color;
        private String nickname;
    }

    public static TodoSummaryInfoResponse of(Todo todo, Onboarding me) {
        List<UserInfo> selectedUsers = todo.getTakes().stream()
                .sorted(Comparator.comparing(take -> take.getOnboarding().getTestScore().getCreatedAt()))
                .map(take -> UserInfo.builder()
                        .onboardingId(take.getOnboarding().getId())
                        .color(take.getOnboarding().getPersonality().getColor())
                        .nickname(take.getOnboarding().getNickname())
                        .build())
                .collect(Collectors.toList());
        for (int i = 0; i < selectedUsers.size(); i++) {
            if (selectedUsers.get(i).getOnboardingId().equals(me.getId())) {
                UserInfo myInfo = selectedUsers.get(i);
                selectedUsers.remove(i);
                selectedUsers.add(0, myInfo);
                break;
            }
        }
        Set<DayOfWeek> dayOfWeekSet = new HashSet<>();
        todo.getTakes().forEach(take -> take.getRedos().forEach(redo -> dayOfWeekSet.add(redo.getDayOfWeek())));
        return TodoSummaryInfoResponse.builder()
                .name(todo.getName())
                .selectedUsers(selectedUsers)
                .dayOfWeeks(new ArrayList<>(dayOfWeekSet))
                .build();
    }
}
