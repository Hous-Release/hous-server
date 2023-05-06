package hous.core.domain.user;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import hous.core.domain.badge.Acquire;
import hous.core.domain.badge.Represent;
import hous.core.domain.common.AuditingTimeEntity;
import hous.core.domain.personality.Personality;
import hous.core.domain.room.Participate;
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
public class Onboarding extends AuditingTimeEntity implements Comparable<Onboarding> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Column(nullable = false, length = 30)
	private String nickname;

	@Column(nullable = false)
	private String birthday;

	@Column(length = 100)
	private String introduction;

	@Column(length = 30)
	private String mbti;

	@Column(length = 30)
	private String job;

	@Column(nullable = false)
	private boolean isPublic;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "personality_id", nullable = false)
	private Personality personality;

	@OneToOne(mappedBy = "onboarding", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private Represent represent;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "test_score_id")
	private TestScore testScore;

	@OneToMany(mappedBy = "onboarding", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private final List<Participate> participates = new ArrayList<>();

	@OneToMany(mappedBy = "onboarding", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private final List<Acquire> acquires = new ArrayList<>();

	public static Onboarding newInstance(User user, Personality personality, String nickname, String birthday,
		boolean isPublic) {
		return builder()
			.user(user)
			.personality(personality)
			.nickname(nickname)
			.birthday(birthday)
			.isPublic(isPublic)
			.build();
	}

	public void updateRepresent(Represent represent) {
		this.represent = represent;
	}

	public void updatePersonality(Personality personality) {
		this.personality = personality;
	}

	public void addParticipate(Participate participate) {
		this.participates.add(participate);
	}

	public void addAcquire(Acquire acquire) {
		this.acquires.add(acquire);
	}

	public void deleteParticipate(Participate participate) {
		this.participates.remove(participate);
	}

	public void setTestScore(TestScore testScore) {
		this.testScore = testScore;
	}

	public void updateUserInfo(String nickname, boolean isPublic, String birthday, String mbti, String job,
		String introduction) {
		this.nickname = nickname;
		this.isPublic = isPublic;
		this.birthday = birthday;
		this.mbti = mbti;
		this.job = job;
		this.introduction = introduction;
		if (introduction != null) {
			this.introduction = introduction.replaceAll("(\r\n|\r|\n|\n\r)", " ");
		}
	}

	public void resetUserInfo() {
		this.isPublic = false;
		this.mbti = null;
		this.job = null;
		this.introduction = null;
		this.testScore = null;
	}

	public void resetBadge() {
		this.represent = null;
		this.acquires.clear();
	}

	@Override
	public int compareTo(Onboarding onboarding) {
		TestScore t1 = getTestScore();
		TestScore t2 = onboarding.getTestScore();
		if (t1 == null && t2 != null) {
			return 1;
		}
		if (t1 != null && t2 == null) {
			return -1;
		}
		if (t1 == null && t2 == null) {
			Participate p1 = getParticipates().get(0);
			Participate p2 = onboarding.getParticipates().get(0);
			return p1.getCreatedAt().compareTo(p2.getCreatedAt());
		}
		return t1.getCreatedAt().compareTo(t2.getCreatedAt());
	}
}
