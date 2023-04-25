package hous.api.service.todo.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(access = AccessLevel.PRIVATE)
public class TodoInfo {
	private Long todoId;
	private String todoName;

	public static TodoInfo of(Long todoId, String todoName) {
		return TodoInfo.builder()
			.todoId(todoId)
			.todoName(todoName)
			.build();
	}
}
