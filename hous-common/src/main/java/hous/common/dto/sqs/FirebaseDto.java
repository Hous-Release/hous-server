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
public class FirebaseDto extends MessageDto {

	private String fcmToken;
	private String title;
	private String body;

	public static FirebaseDto of(String fcmToken, String title, String body) {
		return FirebaseDto.builder()
			.type(MessageType.FIREBASE)
			.fcmToken(fcmToken)
			.title(title)
			.body(body)
			.build();
	}
}
