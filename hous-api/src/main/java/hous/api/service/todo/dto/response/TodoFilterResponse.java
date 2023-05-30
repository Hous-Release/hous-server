package hous.api.service.todo.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import hous.core.domain.todo.Todo;
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
public class TodoFilterResponse {

	private int todosCnt;
	private List<TodoNewInfo> todos;

	public static TodoFilterResponse of(List<Todo> todos, LocalDateTime now) {
		return TodoFilterResponse.builder()
			.todosCnt(todos.size())
			.todos(todos.stream()
				.map(todo -> TodoNewInfo.of(todo, now))
				.collect(Collectors.toList()))
			.build();
	}
}
