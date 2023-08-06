package hous.common.dto.sqs;

import hous.common.constant.MessageType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@SuperBuilder
public class SlackUserFeedbackDto extends MessageDto {

	private String comment;

	public static SlackUserFeedbackDto of(String coment) {
		return SlackUserFeedbackDto.builder()
			.type(MessageType.SLACK_USER_DELETE_FEEDBACK)
			.comment(coment)
			.build();
	}
}
