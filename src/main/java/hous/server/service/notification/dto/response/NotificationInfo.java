package hous.server.service.notification.dto.response;

import hous.server.common.util.DateUtils;
import hous.server.domain.notification.Notification;
import hous.server.domain.notification.NotificationType;
import lombok.*;

import java.time.LocalDateTime;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class NotificationInfo {

    private Long notificationId;
    private NotificationType type;
    private String content;
    private boolean isRead;
    private String createdAt;

    public static NotificationInfo of(Notification notification, LocalDateTime now) {
        return NotificationInfo.builder()
                .notificationId(notification.getId())
                .type(notification.getType())
                .content(notification.getContent())
                .createdAt(DateUtils.passedTime(now, notification.getCreatedAt()))
                .build();
    }
}