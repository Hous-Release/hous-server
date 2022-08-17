package hous.server.service.auth.dto.request;

import hous.server.domain.user.UserSocialType;
import hous.server.service.user.dto.request.CreateUserDto;
import lombok.*;

@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginDto {

    private UserSocialType socialType;

    private String token;

    public static LoginDto of(UserSocialType socialType, String token) {
        return new LoginDto(socialType, token);
    }

    public CreateUserDto toCreateUserDto(String socialId) {
        return CreateUserDto.of(socialId, socialType);
    }
}
