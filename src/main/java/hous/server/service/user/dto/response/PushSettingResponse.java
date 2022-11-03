package hous.server.service.user.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import hous.server.domain.user.PushStatus;
import hous.server.domain.user.Setting;
import hous.server.domain.user.TodoPushStatus;
import lombok.*;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class PushSettingResponse {
    private Boolean isPushNotification;
    private PushStatus rulesPushStatus;
    private TodoPushStatus newTodoPushStatus;
    private TodoPushStatus todayTodoPushStatus;
    private TodoPushStatus remindTodoPushStatus;
    private PushStatus badgePushStatus;

    @JsonProperty("isPushNotification")
    public Boolean isPushNotification() {
        return isPushNotification;
    }

    public static PushSettingResponse of(Setting setting) {
        return PushSettingResponse.builder()
                .isPushNotification(setting.isPushNotification())
                .rulesPushStatus(setting.getRulesPushStatus())
                .newTodoPushStatus(setting.getNewTodoPushStatus())
                .todayTodoPushStatus(setting.getTodayTodoPushStatus())
                .remindTodoPushStatus(setting.getRemindTodoPushStatus())
                .badgePushStatus(setting.getBadgePushStatus())
                .build();
    }
}
