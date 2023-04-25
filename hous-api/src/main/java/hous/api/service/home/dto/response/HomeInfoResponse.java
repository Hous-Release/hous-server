package hous.api.service.home.dto.response;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import hous.api.service.todo.dto.response.OurTodoInfo;
import hous.api.service.todo.dto.response.TodoDetailInfo;
import hous.common.util.DateUtils;
import hous.common.util.MathUtils;
import hous.core.domain.personality.PersonalityColor;
import hous.core.domain.room.Room;
import hous.core.domain.rule.Rule;
import hous.core.domain.todo.OurTodoStatus;
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
public class HomeInfoResponse {

	private String userNickname;
	private String roomName;
	private String roomCode;
	private String dayOfWeek;
	private int progress;
	private int myTodosCnt;
	private List<String> myTodos;
	private List<String> ourRules;
	private List<HomieInfo> homies;

	@ToString
	@Getter
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	@Builder(access = AccessLevel.PRIVATE)
	public static class HomieInfo {
		private Long onboardingId;
		private String userNickname;
		private PersonalityColor color;
	}

	public static HomeInfoResponse of(Onboarding me, Room room, LocalDate today, List<TodoDetailInfo> myTodos,
		List<OurTodoInfo> ourTodos, List<Rule> rules, List<Onboarding> participants) {
		int doneOurTodosCnt = (int)ourTodos.stream()
			.filter(ourTodo -> ourTodo.getStatus() == OurTodoStatus.FULL_CHECK)
			.count();
		return HomeInfoResponse.builder()
			.userNickname(me.getNickname())
			.roomName(room.getName())
			.roomCode(room.getCode())
			.dayOfWeek(DateUtils.nowDayOfWeek(today))
			.progress(MathUtils.percent(doneOurTodosCnt, ourTodos.size()))
			.myTodosCnt(myTodos.size())
			.myTodos(myTodos.stream()
				.limit(3)
				.map(TodoDetailInfo::getTodoName)
				.collect(Collectors.toList()))
			.ourRules(rules.stream()
				.sorted(Comparator.comparing(Rule::getIdx))
				.limit(3)
				.map(Rule::getName)
				.collect(Collectors.toList()))
			.homies(participants.stream()
				.map(onboarding -> HomieInfo.builder()
					.onboardingId(onboarding.getId())
					.userNickname(onboarding.getNickname())
					.color(onboarding.getPersonality().getColor())
					.build())
				.collect(Collectors.toList()))
			.build();
	}
}
