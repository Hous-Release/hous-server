package hous.api.service.notification.dto.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import hous.common.util.DateUtils;
import hous.core.domain.notification.Notification;
import hous.core.domain.notification.NotificationType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

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

	@JsonProperty("isRead")
	public boolean isRead() {
		return isRead;
	}

	public static NotificationInfo of(Notification notification, LocalDateTime now) {
		NotificationInfo notificationInfo = NotificationInfo.builder()
			.notificationId(notification.getId())
			.type(notification.getType())
			.content(notification.getContent())
			.isRead(notification.isRead())
			.createdAt(DateUtils.passedTime(now, notification.getCreatedAt()))
			.build();
		return notificationInfo;
	}
}
