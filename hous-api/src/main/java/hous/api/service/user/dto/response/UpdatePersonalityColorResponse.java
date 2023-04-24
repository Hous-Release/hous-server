package hous.api.service.user.dto.response;

import hous.core.domain.personality.Personality;
import hous.core.domain.personality.PersonalityColor;
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
public class UpdatePersonalityColorResponse {

	private PersonalityColor color;

	public static UpdatePersonalityColorResponse of(Personality personality) {
		return UpdatePersonalityColorResponse.builder()
			.color(personality.getColor())
			.build();
	}
}
