package hous.api.service.todo.dto.request;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CheckTodoRequestDto {

	@ApiModelProperty(value = "요청할 체크 상태", example = "true")
	@NotNull(message = "{todo.status.notNull}")
	private Boolean status;

	public Boolean isStatus() {
		return status;
	}
}
