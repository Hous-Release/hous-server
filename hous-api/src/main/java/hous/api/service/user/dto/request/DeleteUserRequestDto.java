package hous.api.service.user.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import hous.common.constant.Constraint;
import hous.core.domain.feedback.FeedbackType;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

// TODO: 2023/08/01 Deprecated
@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class DeleteUserRequestDto {

	@ApiModelProperty(value = "사유", example = "DONE_LIVING_TOGETHER")
	@NotNull(message = "{user.feedbackType.notNull}")
	private FeedbackType feedbackType;

	@ApiModelProperty(value = "의견", example = "흠냐링 제법 괜찮았으나 안쓰게 되네요.")
	@Size(max = Constraint.FEEDBACK_COMMENT_MAX, message = "{user.comment.max}")
	@NotNull(message = "{user.comment.notNull}")
	private String comment;

	public static DeleteUserRequestDto of(FeedbackType feedbackType, String comment) {
		return DeleteUserRequestDto.builder()
			.feedbackType(feedbackType)
			.comment(comment)
			.build();
	}
}
