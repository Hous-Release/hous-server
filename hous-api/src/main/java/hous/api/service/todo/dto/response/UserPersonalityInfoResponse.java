package hous.api.service.todo.dto.response;

import java.util.List;
import java.util.stream.Collectors;

import hous.core.domain.user.Onboarding;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class UserPersonalityInfoResponse {

	private List<UserPersonalityInfo> users;

	public static UserPersonalityInfoResponse of(List<Onboarding> onboardings) {
		return UserPersonalityInfoResponse.builder()
			.users(onboardings.stream()
				.map(onboarding -> UserPersonalityInfo.of(
					onboarding.getId(),
					onboarding.getPersonality().getColor(),
					onboarding.getNickname()))
				.collect(Collectors.toList()))
			.build();
	}
}
