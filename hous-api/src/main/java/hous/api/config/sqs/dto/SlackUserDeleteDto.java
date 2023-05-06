package hous.api.config.sqs.dto;

import hous.api.service.user.dto.response.UserDeleteResponse;
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
public class SlackUserDeleteDto extends MessageDto {

	private UserDeleteResponse userDeleteResponse;

	public static SlackUserDeleteDto of(UserDeleteResponse userDeleteResponse) {
		return SlackUserDeleteDto.builder()
			.type(MessageType.SLACK_USER_DELETE)
			.userDeleteResponse(userDeleteResponse)
			.build();
	}
}
