package hous.core.domain.rule;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

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

	@Column(nullable = false)
	private int idx;

	public static Rule newInstance(Room room, String name, int idx) {
		return builder()
			.room(room)
			.name(name)
			.idx(idx)
			.build();
	}

	public void updateRule(String name, int idx) {
		this.idx = idx;
		this.name = name;
	}

	@Override
	public int compareTo(@NotNull Rule rule) {
		if (idx == rule.idx) {
			if (rule.getCreatedAt().compareTo(getCreatedAt()) == 0) {
				return Long.compare(id, rule.id);
			}
			return getCreatedAt().compareTo(rule.getCreatedAt());
		}
		return Integer.compare(idx, rule.idx);
	}
}
