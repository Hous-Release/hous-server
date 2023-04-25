package hous.api.service.auth.dto.request;

import hous.core.domain.user.UserSocialType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class LoginDto {

	private UserSocialType socialType;

	private String token;

	private String fcmToken;

	public static LoginDto of(UserSocialType socialType, String token, String fcmToken) {
		return LoginDto.builder()
			.socialType(socialType)
			.token(token)
			.fcmToken(fcmToken)
			.build();
	}
}
