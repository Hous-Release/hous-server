package hous.core.domain.badge;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import hous.core.domain.common.AuditingTimeEntity;
import hous.core.domain.user.Onboarding;
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
public class Acquire extends AuditingTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "onboarding_id")
	private Onboarding onboarding;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "badge_id")
	private Badge badge;

	@Column(nullable = false)
	private boolean isRead;

	public static Acquire newInstance(Onboarding onboarding, Badge badge) {
		return builder()
			.onboarding(onboarding)
			.badge(badge)
			.isRead(false)
			.build();
	}

	public void updateIsRead() {
		this.isRead = true;
	}
}
