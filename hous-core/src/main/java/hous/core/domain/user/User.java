package hous.core.domain.user;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

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
public class User extends AuditingTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Embedded
	private SocialInfo socialInfo;

	@Column(unique = true, length = 300)
	private String fcmToken;

	@Column(nullable = false, length = 30)
	@Enumerated(EnumType.STRING)
	private UserStatus status;

	@OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private Onboarding onboarding;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "setting_id", nullable = false)
	private Setting setting;

	public static User newInstance(String socialId, UserSocialType socialType, Setting setting) {
		return builder()
			.socialInfo(SocialInfo.of(socialId, socialType))
			.setting(setting)
			.status(UserStatus.ACTIVE)
			.build();
	}

	public void setOnboarding(Onboarding onboarding) {
		this.onboarding = onboarding;
	}

	public void updateFcmToken(String fcmToken) {
		this.fcmToken = fcmToken;
	}

	public void resetFcmToken() {
		this.fcmToken = null;
	}
}
