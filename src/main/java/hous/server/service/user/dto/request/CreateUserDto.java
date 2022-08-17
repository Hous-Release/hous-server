package hous.server.service.user.dto.request;

import hous.server.domain.user.UserSocialType;
import lombok.*;

@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateUserDto {

    private String socialId;

    private UserSocialType socialType;

    public static CreateUserDto of(String socialId, UserSocialType socialType) {
        return new CreateUserDto(socialId, socialType);
    }
}
