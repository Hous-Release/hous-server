package hous.server.service.todo.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import hous.server.domain.todo.DayOfWeek;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UpdateTodoRequestDto {

    @ApiModelProperty(value = "todo 이름", example = "청소기 돌리기")
    @NotBlank(message = "{todo.name.notBlank}")
    @Size(max = 15, message = "{todo.name.max}")
    private String name;

    @ApiModelProperty(value = "알림 여부")
    private boolean isPushNotification;

    @ApiModelProperty(value = "담당자 목록")
    @NotEmpty(message = "{todo.todoUsers.notEmpty}")
    private List<@Valid TodoUser> todoUsers;

    @ToString
    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class TodoUser {

        @ApiModelProperty(value = "담당자 id", example = "1")
        private Long onboardingId;

        @ApiModelProperty(value = "담당 요일")
        @NotEmpty(message = "{todo.dayOfWeeks.notEmpty}")
        private List<DayOfWeek> dayOfWeeks;
    }

    @JsonProperty("isPushNotification")
    public boolean isPushNotification() {
        return isPushNotification;
    }
}
