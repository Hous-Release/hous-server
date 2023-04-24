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
public class TodoAllMemberResponse {

	private int totalRoomTodoCnt;
	private List<TodoAllMemberInfo> todos;

	public static TodoAllMemberResponse of(int totalRoomTodoCnt, List<TodoAllMemberInfo> todos) {
		return TodoAllMemberResponse.builder()
			.totalRoomTodoCnt(totalRoomTodoCnt)
			.todos(todos)
			.build();
	}
}
