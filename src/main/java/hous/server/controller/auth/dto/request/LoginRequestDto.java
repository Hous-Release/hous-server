package hous.server.controller.auth.dto.request;

import hous.server.domain.user.UserSocialType;
import hous.server.service.auth.dto.request.LoginDto;
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

    public LoginDto toServiceDto() {
        return LoginDto.of(socialType, token);
    }
}
