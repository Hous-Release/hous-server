package hous.server.service.user.dto.request;

import hous.server.domain.user.UserSocialType;
import lombok.*;

@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateUserRequestDto {

    private String socialId;

    private UserSocialType socialType;

    private String fcmToken;

    public static CreateUserRequestDto of(String socialId, UserSocialType socialType, String fcmToken) {
        return new CreateUserRequestDto(socialId, socialType, fcmToken);
    }
}
