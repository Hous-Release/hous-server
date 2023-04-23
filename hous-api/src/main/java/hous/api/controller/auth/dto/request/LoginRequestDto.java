package hous.api.controller.auth.dto.request;

import hous.api.service.auth.dto.request.LoginDto;
import hous.core.domain.user.UserSocialType;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginRequestDto {

    @ApiModelProperty(value = "소셜 로그인 타입 - KAKAO, APPLE", example = "KAKAO")
    @NotNull(message = "{user.socialType.notNull}")
    private UserSocialType socialType;

    @ApiModelProperty(value = "토큰 - socialToken", example = "ijv4qLk0I7jYuDpFe-9A-oAx59-AAfC6UbTuairPCj1zTQAAAYI6e-6o")
    @NotBlank(message = "{auth.token.notBlank}")
    private String token;

    @ApiModelProperty(value = "토큰 - fcmToken", example = "dfdafjdslkfjslfjslifsjvmdsklvdosijiofjamvsdlkvmiodsjfdiosmvsdjvosadjvosd")
    @NotBlank(message = "{auth.fcmToken.notBlank}")
    private String fcmToken;

    public LoginDto toServiceDto() {
        return LoginDto.of(socialType, token, fcmToken);
    }
}
