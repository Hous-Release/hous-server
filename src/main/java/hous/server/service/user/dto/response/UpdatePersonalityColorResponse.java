package hous.server.service.user.dto.response;

import hous.server.domain.personality.Personality;
import hous.server.domain.personality.PersonalityColor;
import lombok.*;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class UpdatePersonalityColorResponse {

    private PersonalityColor color;

    public static UpdatePersonalityColorResponse of(Personality personality) {
        return UpdatePersonalityColorResponse.builder()
                .color(personality.getColor())
                .build();
    }
}
