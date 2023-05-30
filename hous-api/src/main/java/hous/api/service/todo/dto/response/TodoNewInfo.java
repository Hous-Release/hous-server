package hous.api.service.todo.dto.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import hous.core.domain.todo.Todo;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TodoNewInfo extends TodoInfo {

	private boolean isNew;

	@JsonProperty("isNew")
	public boolean isNew() {
		return isNew;
	}

	@Builder(access = AccessLevel.PRIVATE)
	public TodoNewInfo(Long todoId, String todoName, boolean isNew) {
		super(todoId, todoName);
		this.isNew = isNew;
	}

	public static TodoNewInfo of(Todo todo, LocalDateTime now) {
		return TodoNewInfo.builder()
			.todoId(todo.getId())
			.todoName(todo.getName())
			.isNew(now.isBefore(todo.getCreatedAt().plusHours(12)))
			.build();
	}
}
