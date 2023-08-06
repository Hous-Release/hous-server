package hous.api.service.user.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import hous.common.constant.Constraint;
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
public class UserFeedbackRequestDto {

	@ApiModelProperty(value = "의견", example = "흠냐링 제법 괜찮았으나 안쓰게 되네요.")
	@Size(max = Constraint.FEEDBACK_COMMENT_MAX, message = "{user.comment.max}")
	@NotBlank(message = "{user.comment.notBlank}")
	private String comment;

	@ApiModelProperty(value = "회원 탈퇴 여부", example = "false")
	private boolean isDeleting;

	@JsonProperty("isDeleting")
	public boolean isDeleting() {
		return isDeleting;
	}
}
