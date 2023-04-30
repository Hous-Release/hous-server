package hous.core.domain.delete;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import hous.core.domain.common.AuditingTimeEntity;
import hous.core.domain.notification.NotificationType;
import hous.core.domain.user.Onboarding;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// TODO migration 엔티티 - 삭제 예정

@Getter
@Entity(name = "notification")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class RdbNotification extends AuditingTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "onboarding_id")
	private Onboarding onboarding;

	@Column(nullable = false, length = 30)
	@Enumerated(EnumType.STRING)
	private NotificationType type;

	@Column(nullable = false, length = 100)
	private String content;

	@Column(nullable = false)
	private boolean isRead;

	public static RdbNotification newInstance(Onboarding onboarding, NotificationType type, String content,
		boolean isRead) {
		return RdbNotification.builder()
			.onboarding(onboarding)
			.type(type)
			.content(content)
			.isRead(isRead)
			.build();
	}

	public void updateIsRead() {
		this.isRead = true;
	}
}
