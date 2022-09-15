package hous.server.service.auth.dto.request;

import hous.server.domain.user.UserSocialType;
import hous.server.service.user.dto.request.CreateUserRequestDto;
import lombok.*;

@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginDto {

    private UserSocialType socialType;

    private String token;

    private String fcmToken;

    public static LoginDto of(UserSocialType socialType, String token, String fcmToken) {
        return new LoginDto(socialType, token, fcmToken);
    }

    public CreateUserRequestDto toCreateUserDto(String socialId) {
        return CreateUserRequestDto.of(socialId, socialType, fcmToken);
    }
}
