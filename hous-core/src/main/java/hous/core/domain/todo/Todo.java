package hous.core.domain.todo;

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
public class Todo extends AuditingTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "room_id")
	private Room room;

	@Column(nullable = false, length = 100)
	private String name;

	@Column(nullable = false)
	private boolean isPushNotification;

	@OneToMany(mappedBy = "todo", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private final List<Take> takes = new ArrayList<>();

	@OneToMany(mappedBy = "todo", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private final List<Done> dones = new ArrayList<>();

	public static Todo newInstance(Room room, String name, boolean isPushNotification) {
		return builder()
			.room(room)
			.name(name)
			.isPushNotification(isPushNotification)
			.build();
	}

	public void updateTodo(String name, boolean isPushNotification, List<Take> takes) {
		this.name = name;
		this.isPushNotification = isPushNotification;
		this.takes.clear();
		this.takes.addAll(takes);
	}

	public void addTake(Take take) {
		this.takes.add(take);
	}

	public void addDone(Done done) {
		this.dones.add(done);
	}

	public void deleteTake(Take take) {
		this.takes.remove(take);
	}

	public void deleteDone(Done done) {
		this.dones.remove(done);
	}
}
