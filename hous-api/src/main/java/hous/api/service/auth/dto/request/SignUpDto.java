package hous.api.service.auth.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import hous.api.service.user.dto.request.CreateUserRequestDto;
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
public class SignUpDto {

	private UserSocialType socialType;
	private String token;
	private String fcmToken;
	private String nickname;
	private String birthday;
	private Boolean isPublic;

	@JsonProperty("isPublic")
	public Boolean isPublic() {
		return isPublic;
	}

	public static SignUpDto of(UserSocialType socialType, String token, String fcmToken,
		String nickname, String birthday, Boolean isPublic) {
		return SignUpDto.builder()
			.socialType(socialType)
			.token(token)
			.fcmToken(fcmToken)
			.nickname(nickname)
			.birthday(birthday)
			.isPublic(isPublic)
			.build();
	}

	public CreateUserRequestDto toCreateUserDto(String socialId) {
		return CreateUserRequestDto.of(socialId, socialType, fcmToken, nickname, birthday, isPublic);
	}
}
