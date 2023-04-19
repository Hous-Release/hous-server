package hous.api.service.user.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import hous.core.domain.user.UserSocialType;
import lombok.*;

@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class CreateUserRequestDto {

    private String socialId;
    private UserSocialType socialType;
    private String fcmToken;
    private String nickname;
    private String birthday;
    private Boolean isPublic;

    @JsonProperty("isPublic")
    public Boolean isPublic() {
        return isPublic;
    }

    public static CreateUserRequestDto of(String socialId, UserSocialType socialType, String fcmToken,
                                          String nickname, String birthday, Boolean isPublic) {
        return CreateUserRequestDto.builder()
                .socialId(socialId)
                .socialType(socialType)
                .fcmToken(fcmToken)
                .nickname(nickname)
                .birthday(birthday)
                .isPublic(isPublic)
                .build();
    }
}
