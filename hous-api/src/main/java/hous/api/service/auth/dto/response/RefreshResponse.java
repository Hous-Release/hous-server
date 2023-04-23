package hous.api.service.auth.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class RefreshResponse {

    private TokenResponse token;
    private boolean isJoiningRoom;

    @JsonProperty("isJoiningRoom")
    public boolean isJoiningRoom() {
        return isJoiningRoom;
    }

    public static RefreshResponse of(TokenResponse token, boolean isJoiningRoom) {
        return RefreshResponse.builder()
                .token(token)
                .isJoiningRoom(isJoiningRoom)
                .build();
    }
}
