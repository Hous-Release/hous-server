package hous.api.service.todo.dto.response;

import java.util.List;

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
public class DayOfWeekTodo {
	private String dayOfWeek;
	private int todoCnt;
	private List<TodoInfo> dayOfWeekTodos;

	public static DayOfWeekTodo of(String dayOfWeek, int todoCnt, List<TodoInfo> dayOfWeekTodos) {
		return DayOfWeekTodo.builder()
			.dayOfWeek(dayOfWeek)
			.todoCnt(todoCnt)
			.dayOfWeekTodos(dayOfWeekTodos)
			.build();
	}
}
