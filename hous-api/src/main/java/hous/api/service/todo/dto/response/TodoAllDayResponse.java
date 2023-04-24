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
public class TodoAllDayResponse {

	private int totalRoomTodoCnt;
	private List<TodoAllDayInfo> todos;

	public static TodoAllDayResponse of(int totalRoomTodoCnt, List<TodoAllDayInfo> todos) {
		return TodoAllDayResponse.builder()
			.totalRoomTodoCnt(totalRoomTodoCnt)
			.todos(todos)
			.build();
	}
}
