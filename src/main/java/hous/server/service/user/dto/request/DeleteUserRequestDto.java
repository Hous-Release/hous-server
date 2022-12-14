package hous.server.service.user.dto.request;

import hous.server.domain.common.Constraint;
import hous.server.domain.feedback.FeedbackType;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DeleteUserRequestDto {

    @ApiModelProperty(value = "사유", example = "DONE_LIVING_TOGETHER")
    @NotNull(message = "{user.feedbackType.notNull}")
    private FeedbackType feedbackType;

    @ApiModelProperty(value = "의견", example = "흠냐링 제법 괜찮았으나 안쓰게 되네요.")
    @Size(max = Constraint.FEEDBACK_COMMENT_MAX, message = "{user.comment.max}")
    @NotNull(message = "{user.comment.notNull}")
    private String comment;
}
