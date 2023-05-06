package hous.api.service.todo.dto.response;

import java.util.List;

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
public class TodoAllMemberInfo {

	private String userName;
	private PersonalityColor color;
	private int totalTodoCnt;
	private List<DayOfWeekTodo> dayOfWeekTodos;

	public static TodoAllMemberInfo of(String userName, PersonalityColor color, int totalTodoCnt,
		List<DayOfWeekTodo> dayOfWeekTodos) {
		return TodoAllMemberInfo.builder()
			.userName(userName)
			.color(color)
			.totalTodoCnt(totalTodoCnt)
			.dayOfWeekTodos(dayOfWeekTodos)
			.build();
	}
}
