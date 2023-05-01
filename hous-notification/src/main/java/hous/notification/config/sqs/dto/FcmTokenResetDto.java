package hous.notification.config.sqs.dto;

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
public class FcmTokenResetDto extends MessageDto {

	private String fcmToken;

	public static FcmTokenResetDto of(String fcmToken) {
		return FcmTokenResetDto.builder()
			.type(MessageType.FCM_TOKEN_RESET)
			.fcmToken(fcmToken)
			.build();
	}
}
