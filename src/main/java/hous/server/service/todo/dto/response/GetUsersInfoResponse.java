package hous.server.service.todo.dto.response;

import hous.server.domain.user.Onboarding;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class GetUsersInfoResponse {

    private List<UserInfo> users;

    @ToString
    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder(access = AccessLevel.PRIVATE)
    private static class UserInfo {
        private Long onboardingId;
        private String color;
        private String nickname;
    }

    public static GetUsersInfoResponse of(List<Onboarding> onboardings) {
        return GetUsersInfoResponse.builder()
                .users(onboardings.stream()
                        .map(onboarding -> UserInfo.builder()
                                .onboardingId(onboarding.getId())
                                .color(onboarding.getPersonality().getColor().toString())
                                .nickname(onboarding.getNickname())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}
