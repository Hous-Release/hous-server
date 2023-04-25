package hous.core.domain.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import hous.core.domain.common.AuditingTimeEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
		return builder()
			.isPushNotification(true)
			.rulesPushStatus(PushStatus.ON)
			.newTodoPushStatus(TodoPushStatus.ON_ALL)
			.todayTodoPushStatus(TodoPushStatus.ON_ALL)
			.remindTodoPushStatus(TodoPushStatus.ON_ALL)
			.badgePushStatus(PushStatus.ON)
			.build();
	}

	public void updatePushSetting(Boolean isPushNotification, PushStatus rulesPushStatus,
		TodoPushStatus newTodoPushStatus, TodoPushStatus todayTodoPushStatus, TodoPushStatus remindTodoPushStatus,
		PushStatus badgePushStatus) {
		if (isPushNotification != null) {
			this.isPushNotification = isPushNotification;
		}
		if (rulesPushStatus != null) {
			this.rulesPushStatus = rulesPushStatus;
		}
		if (newTodoPushStatus != null) {
			this.newTodoPushStatus = newTodoPushStatus;
		}
		if (todayTodoPushStatus != null) {
			this.todayTodoPushStatus = todayTodoPushStatus;
		}
		if (remindTodoPushStatus != null) {
			this.remindTodoPushStatus = remindTodoPushStatus;
		}
		if (badgePushStatus != null) {
			this.badgePushStatus = badgePushStatus;
		}
	}
}
