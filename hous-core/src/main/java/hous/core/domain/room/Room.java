package hous.core.domain.room;

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

import hous.core.domain.common.AuditingTimeEntity;
import hous.core.domain.rule.Rule;
import hous.core.domain.todo.Todo;
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
public class Room extends AuditingTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "onboarding_id", nullable = false)
	private Onboarding owner;

	@Column(nullable = false, length = 30)
	private String name;

	@Column(nullable = false, length = 30)
	private String code;

	@Column(nullable = false)
	private int participantsCnt;

	@OneToMany(mappedBy = "room", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private final List<Participate> participates = new ArrayList<>();

	@OneToMany(mappedBy = "room", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private final List<Rule> rules = new ArrayList<>();

	@OneToMany(mappedBy = "room", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private final List<Todo> todos = new ArrayList<>();

	public static Room newInstance(Onboarding owner, String name, String code) {
		return builder()
			.owner(owner)
			.name(name)
			.code(code)
			.participantsCnt(0)
			.build();
	}

	public void updateOwner(Onboarding onboarding) {
		this.owner = onboarding;
	}

	public void updateRoomName(String name) {
		this.name = name;
	}

	public void addParticipate(Participate participate) {
		this.participates.add(participate);
		this.participantsCnt += 1;
	}

	public void deleteParticipate(Participate participate) {
		this.participates.remove(participate);
		this.participantsCnt -= 1;
	}

	public void addRules(List<Rule> rules) {
		this.rules.addAll(rules);
	}

	public void updateRule(Rule rule) {
		this.rules.set(rules.indexOf(rule), rule);
	}

	public void deleteRule(Rule rule) {
		this.rules.remove(rule);
	}

	public void addTodo(Todo todo) {
		this.todos.add(todo);
	}

	public void updateTodo(Todo todo) {
		this.todos.set(todos.indexOf(todo), todo);
	}

	public void deleteTodo(Todo todo) {
		this.todos.remove(todo);
	}
}
