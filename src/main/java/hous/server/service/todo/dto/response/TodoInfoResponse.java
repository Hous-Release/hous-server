package hous.server.service.todo.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import hous.server.domain.personality.PersonalityColor;
import hous.server.domain.todo.DayOfWeek;
import hous.server.domain.todo.Redo;
import hous.server.domain.todo.Take;
import hous.server.domain.todo.Todo;
import hous.server.domain.user.Onboarding;
import lombok.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class TodoInfoResponse {

    private String name;
    private boolean isPushNotification;
    private List<UserInfo> selectedUsers;
    private List<TodoUser> todoUsers;

    @JsonProperty("isPushNotification")
    public boolean isPushNotification() {
        return isPushNotification;
    }

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

    @ToString
    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder(access = AccessLevel.PRIVATE)
    private static class TodoUser {
        private Long onboardingId;
        private PersonalityColor color;
        private String nickname;
        private boolean isSelected;
        private List<DayOfWeek> dayOfWeeks;

        @JsonProperty("isSelected")
        public boolean isSelected() {
            return isSelected;
        }
    }

    public static TodoInfoResponse of(Todo todo, List<Onboarding> onboardings) {
        List<UserInfo> selectedUsers = todo.getTakes().stream()
                .sorted(Comparator.comparing(take -> take.getOnboarding().getTestScore().getCreatedAt()))
                .map(take -> UserInfo.builder()
                        .onboardingId(take.getOnboarding().getId())
                        .color(take.getOnboarding().getPersonality().getColor())
                        .nickname(take.getOnboarding().getNickname())
                        .build())
                .collect(Collectors.toList());
        List<TodoUser> todoUsers = onboardings.stream()
                .map(onboarding -> {
                    List<Take> takes = todo.getTakes().stream()
                            .filter(take -> take.getOnboarding().getId().equals(onboarding.getId()))
                            .collect(Collectors.toList());
                    return TodoUser.builder()
                            .onboardingId(onboarding.getId())
                            .color(onboarding.getPersonality().getColor())
                            .nickname(onboarding.getNickname())
                            .isSelected(takes.size() == 1)
                            .dayOfWeeks(takes.size() == 1 ? takes.get(0).getRedos().stream()
                                    .map(Redo::getDayOfWeek)
                                    .collect(Collectors.toList()) : new ArrayList<>())
                            .build();
                })
                .collect(Collectors.toList());
        return TodoInfoResponse.builder()
                .name(todo.getName())
                .isPushNotification(todo.isPushNotification())
                .selectedUsers(selectedUsers)
                .todoUsers(todoUsers)
                .build();
    }
}
