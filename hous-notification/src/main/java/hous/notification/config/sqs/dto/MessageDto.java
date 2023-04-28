package hous.notification.config.sqs.dto;

import hous.core.domain.user.User;
import hous.notification.service.slack.dto.response.UserDeleteResponse;
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
}
