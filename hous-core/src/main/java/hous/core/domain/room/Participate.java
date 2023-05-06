package hous.core.domain.room;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.jetbrains.annotations.NotNull;

import hous.core.domain.common.AuditingTimeEntity;
import hous.core.domain.user.Onboarding;
import hous.core.domain.user.TestScore;
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
public class Participate extends AuditingTimeEntity implements Comparable<Participate> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "onboarding_id")
	private Onboarding onboarding;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "room_id")
	private Room room;

	public static Participate newInstance(Onboarding onboarding, Room room) {
		return builder()
			.onboarding(onboarding)
			.room(room)
			.build();
	}

	@Override
	public int compareTo(@NotNull Participate participate) {
		TestScore t1 = getOnboarding().getTestScore();
		TestScore t2 = participate.getOnboarding().getTestScore();
		if (t1 == null && t2 != null) {
			return 1;
		}
		if (t1 != null && t2 == null) {
			return -1;
		}
		if (t1 == null && t2 == null) {
			Participate p1 = getOnboarding().getParticipates().get(0);
			Participate p2 = participate.getOnboarding().getParticipates().get(0);
			return p1.getCreatedAt().compareTo(p2.getCreatedAt());
		}
		return t1.getCreatedAt().compareTo(t2.getCreatedAt());
	}
}
