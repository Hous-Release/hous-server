package hous.server.service.todo.dto.response;

import hous.server.domain.personality.PersonalityColor;
import hous.server.domain.user.Onboarding;
import lombok.*;

import java.util.List;

@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(access = AccessLevel.PRIVATE)
public class UserPersonalityInfo {

    private Long onboardingId;
    private PersonalityColor color;
    private String nickname;

    public static UserPersonalityInfo of(Long onboardingId, PersonalityColor color, String nickname) {
        return UserPersonalityInfo.builder()
                .onboardingId(onboardingId)
                .color(color)
                .nickname(nickname)
                .build();
    }

    public static List<UserPersonalityInfo> sortMeFirst(List<UserPersonalityInfo> userPersonalityInfos, Onboarding me) {
        for (int i = 0; i < userPersonalityInfos.size(); i++) {
            if (userPersonalityInfos.get(i).getOnboardingId().equals(me.getId())) {
                UserPersonalityInfo myInfo = userPersonalityInfos.get(i);
                userPersonalityInfos.remove(i);
                userPersonalityInfos.add(0, myInfo);
                break;
            }
        }
        return userPersonalityInfos;
    }
}
