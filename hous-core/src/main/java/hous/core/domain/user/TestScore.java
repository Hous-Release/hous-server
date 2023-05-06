package hous.core.domain.user;

import javax.persistence.Column;
import javax.persistence.Entity;
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
public class TestScore extends AuditingTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private int light;

	@Column(nullable = false)
	private int noise;

	@Column(nullable = false)
	private int clean;

	@Column(nullable = false)
	private int smell;

	@Column(nullable = false)
	private int introversion;

	public static TestScore newInstance() {
		return builder()
			.light(0)
			.noise(0)
			.clean(0)
			.smell(0)
			.introversion(0)
			.build();
	}

	public void updateTestScore(int light, int noise, int clean, int smell, int introversion) {
		this.light = light;
		this.noise = noise;
		this.clean = clean;
		this.smell = smell;
		this.introversion = introversion;
	}

	public int getTotalTestScore() {
		return this.getLight() + this.getNoise() + this.getClean() + this.getSmell() + this.getIntroversion();
	}
}
