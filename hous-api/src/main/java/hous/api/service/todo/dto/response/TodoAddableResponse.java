package hous.api.service.todo.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

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
public class TodoAddableResponse {

	private boolean isAddable;

	@JsonProperty("isAddable")
	public boolean isAddable() {
		return isAddable;
	}

	public static TodoAddableResponse of(boolean isAddable) {
		return TodoAddableResponse.builder()
			.isAddable(isAddable)
			.build();
	}
}
