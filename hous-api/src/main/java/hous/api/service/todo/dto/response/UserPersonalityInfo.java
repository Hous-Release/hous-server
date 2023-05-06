package hous.api.service.todo.dto.response;

import java.util.List;

import hous.core.domain.personality.PersonalityColor;
import hous.core.domain.user.Onboarding;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
