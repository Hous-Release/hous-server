package hous.server.service.user.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class CheckOnboardingInfoResponse {

    private boolean isChecked;

    @JsonProperty("isChecked")
    public boolean isChecked() {
        return isChecked;
    }

    public static CheckOnboardingInfoResponse of(boolean isChecked) {
        return CheckOnboardingInfoResponse.builder()
                .isChecked(isChecked)
                .build();
    }
}
