package hous.api.service.todo.dto.request;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import hous.common.constant.Constraint;
import hous.core.domain.todo.DayOfWeek;
import io.swagger.annotations.ApiModelProperty;
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
public class TodoInfoRequestDto {

	@ApiModelProperty(value = "todo 이름", example = "청소기 돌리기")
	@NotBlank(message = "{todo.name.notBlank}")
	@Size(max = Constraint.TODO_NAME_MAX, message = "{todo.name.max}")
	private String name;

	@ApiModelProperty(value = "알림 여부", example = "true")
	@NotNull(message = "{todo.isPushNotification.notNull}")
	private Boolean isPushNotification;

	@ApiModelProperty(value = "담당자 목록")
	@NotEmpty(message = "{todo.todoUsers.notEmpty}")
	private List<@Valid TodoUser> todoUsers;

	@ToString
	@Getter
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	@Builder
	public static class TodoUser {

		@ApiModelProperty(value = "담당자 id", example = "1")
		private Long onboardingId;

		@ApiModelProperty(value = "담당 요일")
		@NotEmpty(message = "{todo.dayOfWeeks.notEmpty}")
		private List<DayOfWeek> dayOfWeeks;
	}

	@JsonProperty("isPushNotification")
	public Boolean isPushNotification() {
		return isPushNotification;
	}

	public static TodoInfoRequestDto of(String name, Boolean isPushNotification, List<TodoUser> todoUsers) {
		return TodoInfoRequestDto.builder()
			.name(name)
			.isPushNotification(isPushNotification)
			.todoUsers(todoUsers)
			.build();
	}
}
