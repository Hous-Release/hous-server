package hous.api.config.sqs.dto;

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
public class SlackExceptionDto extends MessageDto {

	private Exception exception;

	public static SlackExceptionDto of(Exception exception) {
		return SlackExceptionDto.builder()
			.type(MessageType.SLACK_EXCEPTION)
			.exception(exception)
			.build();
	}
}
