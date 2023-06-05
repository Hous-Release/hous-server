package hous.core.domain.rule;

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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.jetbrains.annotations.NotNull;

import hous.core.domain.common.AuditingTimeEntity;
import hous.core.domain.room.Room;
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
public class Rule extends AuditingTimeEntity implements Comparable<Rule> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "room_id")
	private Room room;

	@Column(nullable = false, length = 100)
	private String name;

	// TODO 업데이트 이후 삭제하던가 논의해보기
	@Column(nullable = false)
	private int idx;

	@Column(length = 100)
	private String description;

	@Column(nullable = false)
	private boolean isRepresent;

	@OneToMany(mappedBy = "rule", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private final List<RuleImage> images = new ArrayList<>();

	public static Rule newInstance(Room room, String name, int idx, String description) {
		return builder()
			.room(room)
			.name(name)
			.idx(idx)
			.description(description)
			.build();
	}

	// TODO deprecated
	public void updateRule(String name, int idx) {
		this.idx = idx;
		this.name = name;
	}

	public void updateRule(String name, int idx, String description) {
		this.idx = idx;
		this.name = name;
		this.description = description;
	}

	public void addAllRuleImage(List<RuleImage> images) {
		this.images.addAll(images);
	}

	@Override
	public int compareTo(@NotNull Rule rule) {
		return getCreatedAt().compareTo(rule.getCreatedAt());
	}
}
