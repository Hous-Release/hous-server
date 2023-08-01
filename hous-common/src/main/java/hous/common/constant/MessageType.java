package hous.common.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MessageType {

	public static final String TYPE = "TYPE";
	public static final String FIREBASE = "FIREBASE";
	public static final String FCM_TOKEN_RESET = "FCM_TOKEN_RESET";
	public static final String SLACK_EXCEPTION = "SLACK_EXCEPTION";
	public static final String SLACK_USER_DELETE = "SLACK_USER_DELETE";
	public static final String SLACK_USER_DELETE_FEEDBACK = "SLACK_USER_DELETE_FEEDBACK";
}
