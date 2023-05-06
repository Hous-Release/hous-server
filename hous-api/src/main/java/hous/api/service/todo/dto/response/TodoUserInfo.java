package hous.api.service.todo.dto.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import hous.core.domain.personality.PersonalityColor;
import hous.core.domain.todo.DayOfWeek;
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
public class TodoUserInfo extends UserPersonalityInfo {

	private boolean isSelected;
	private List<DayOfWeek> dayOfWeeks;

	@JsonProperty("isSelected")
	public boolean isSelected() {
		return isSelected;
	}

	@Builder(access = AccessLevel.PRIVATE)
	public TodoUserInfo(Long onboardingId, PersonalityColor color, String nickname, boolean isSelected,
		List<DayOfWeek> dayOfWeeks) {
		super(onboardingId, color, nickname);
		this.isSelected = isSelected;
		this.dayOfWeeks = dayOfWeeks;
	}

	public static TodoUserInfo of(Long onboardingId, PersonalityColor color, String nickname, boolean isSelected,
		List<DayOfWeek> dayOfWeeks) {
		return TodoUserInfo.builder()
			.onboardingId(onboardingId)
			.color(color)
			.nickname(nickname)
			.isSelected(isSelected)
			.dayOfWeeks(dayOfWeeks)
			.build();
	}
}
