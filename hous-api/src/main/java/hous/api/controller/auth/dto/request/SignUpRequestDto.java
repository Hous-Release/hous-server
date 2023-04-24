package hous.api.controller.auth.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import hous.api.service.auth.dto.request.SignUpDto;
import hous.common.constant.Constraint;
import hous.core.domain.user.UserSocialType;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SignUpRequestDto {

	@ApiModelProperty(value = "소셜 로그인 타입 - KAKAO, APPLE", example = "KAKAO")
	@NotNull(message = "{user.socialType.notNull}")
	private UserSocialType socialType;

	@ApiModelProperty(value = "토큰 - socialToken", example = "ijv4qLk0I7jYuDpFe-9A-oAx59-AAfC6UbTuairPCj1zTQAAAYI6e-6o")
	@NotBlank(message = "{auth.token.notBlank}")
	private String token;

	@ApiModelProperty(value = "토큰 - fcmToken", example = "dfdafjdslkfjslfjslifsjvmdsklvdosijsmvsdjvosadjvosd")
	@NotBlank(message = "{auth.fcmToken.notBlank}")
	private String fcmToken;

	@ApiModelProperty(value = "닉네임", example = "혜조니")
	@NotBlank(message = "{onboarding.nickname.notBlank}")
	@Size(max = Constraint.ONBOARDING_NICKNAME_MAX, message = "{onboarding.nickname.max}")
	private String nickname;

	@ApiModelProperty(value = "생년월일", example = "1999-03-04")
	@NotNull(message = "{onboarding.birthday.notNull}")
	private String birthday;

	@ApiModelProperty(value = "생년월일 공개 여부", example = "true")
	@NotNull(message = "{onboarding.isPublic.notNull}")
	private Boolean isPublic;

	@JsonProperty("isPublic")
	public Boolean isPublic() {
		return isPublic;
	}

	public SignUpDto toServiceDto() {
		return SignUpDto.of(socialType, token, fcmToken, nickname, birthday, isPublic);
	}
}
