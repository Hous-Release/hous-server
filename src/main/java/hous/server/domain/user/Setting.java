package hous.server.domain.user;

import hous.server.domain.common.AuditingTimeEntity;
import hous.server.service.user.dto.request.UpdatePushSettingRequestDto;
import lombok.*;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class Setting extends AuditingTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private boolean isPushNotification;

    @Column(nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private PushStatus rulesPushStatus;

    @Column(nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private TodoPushStatus newTodoPushStatus;

    @Column(nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private TodoPushStatus todayTodoPushStatus;

    @Column(nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private TodoPushStatus remindTodoPushStatus;

    @Column(nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private PushStatus badgePushStatus;

    public static Setting newInstance() {
        return Setting.builder()
                .isPushNotification(true)
                .rulesPushStatus(PushStatus.ON)
                .newTodoPushStatus(TodoPushStatus.ON_ALL)
                .todayTodoPushStatus(TodoPushStatus.ON_ALL)
                .remindTodoPushStatus(TodoPushStatus.ON_ALL)
                .badgePushStatus(PushStatus.ON)
                .build();
    }

    public void updatePushSetting(UpdatePushSettingRequestDto request) {
        this.isPushNotification = request.isPushNotification();
        this.rulesPushStatus = request.getRulesPushStatus();
        this.newTodoPushStatus = request.getNewTodoPushStatus();
        this.todayTodoPushStatus = request.getTodayTodoPushStatus();
        this.remindTodoPushStatus = request.getRemindTodoPushStatus();
        this.badgePushStatus = request.getBadgePushStatus();
    }
}
