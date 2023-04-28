package hous.api.config.sqs.dto;

import hous.api.service.user.dto.response.UserDeleteResponse;
import hous.common.constant.MessageType;
import hous.core.domain.user.User;
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
public class MessageDto {

	private String type;
	// FIREBASE
	private User to;
	private String title;
	private String body;
	// SLACK_EXCEPTION
	private Exception exception;
	// SLACK_USER_DELETE
	private UserDeleteResponse userDeleteResponse;

	public static MessageDto of(User to, String title, String body) {
		return MessageDto.builder()
			.type(MessageType.FIREBASE)
			.to(to)
			.title(title)
			.body(body)
			.exception(null)
			.userDeleteResponse(null)
			.build();
	}

	public static MessageDto of(Exception exception) {
		return MessageDto.builder()
			.type(MessageType.SLACK_EXCEPTION)
			.to(null)
			.title(null)
			.body(null)
			.exception(exception)
			.userDeleteResponse(null)
			.build();
	}

	public static MessageDto of(UserDeleteResponse userDeleteResponse) {
		return MessageDto.builder()
			.type(MessageType.SLACK_USER_DELETE)
			.to(null)
			.title(null)
			.body(null)
			.exception(null)
			.userDeleteResponse(userDeleteResponse)
			.build();
	}
}
