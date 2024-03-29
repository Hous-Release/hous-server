package hous.api.service.todo.dto.response;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;

import hous.core.domain.todo.Redo;
import hous.core.domain.todo.Take;
import hous.core.domain.todo.Todo;
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
public class TodoInfoResponse {

	private String name;
	private boolean isPushNotification;
	private List<UserPersonalityInfo> selectedUsers;
	private List<TodoUserInfo> todoUsers;

	@JsonProperty("isPushNotification")
	public boolean isPushNotification() {
		return isPushNotification;
	}

	public static TodoInfoResponse of(Todo todo, List<UserPersonalityInfo> userPersonalityInfos,
		List<Onboarding> onboardings) {
		List<TodoUserInfo> todoUsers = toTodoInfoList(todo, onboardings);
		return TodoInfoResponse.builder()
			.name(todo.getName())
			.isPushNotification(todo.isPushNotification())
			.selectedUsers(userPersonalityInfos)
			.todoUsers(todoUsers)
			.build();
	}

	private static List<TodoUserInfo> toTodoInfoList(Todo todo, List<Onboarding> onboardings) {
		return onboardings.stream()
			.map(onboarding -> {
				Optional<Take> myTake = todo.getTakes().stream()
					.filter(take -> take.getOnboarding().getId().equals(onboarding.getId()))
					.findFirst();
				return TodoUserInfo.of(
					onboarding.getId(),
					onboarding.getPersonality().getColor(),
					onboarding.getNickname(),
					myTake.isPresent(),
					myTake.map(take -> take.getRedos().stream()
						.map(Redo::getDayOfWeek)
						.collect(Collectors.toList())).orElseGet(ArrayList::new)
				);
			})
			.collect(Collectors.toList());
	}
}
