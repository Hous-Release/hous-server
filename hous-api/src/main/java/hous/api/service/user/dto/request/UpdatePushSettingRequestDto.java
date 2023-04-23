package hous.api.service.user.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import hous.core.domain.user.PushStatus;
import hous.core.domain.user.TodoPushStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UpdatePushSettingRequestDto {

    @ApiModelProperty(value = "알림 받기", example = "true")
    private Boolean isPushNotification;

    @ApiModelProperty(value = "새로운 Rules 추가", example = "ON")
    private PushStatus rulesPushStatus;

    @ApiModelProperty(value = "새로운 to-do 추가 알림", example = "ON_ALL")
    private TodoPushStatus newTodoPushStatus;

    @ApiModelProperty(value = "오늘의 to-do 시작 알림", example = "ON_MY")
    private TodoPushStatus todayTodoPushStatus;

    @ApiModelProperty(value = "미완료 to-do 알림", example = "OFF")
    private TodoPushStatus remindTodoPushStatus;

    @ApiModelProperty(value = "배지 알림", example = "ON")
    private PushStatus badgePushStatus;

    @JsonProperty("isPushNotification")
    public Boolean isPushNotification() {
        return isPushNotification;
    }
}
