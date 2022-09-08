package hous.server.domain.room;

import hous.server.domain.common.AuditingTimeEntity;
import hous.server.domain.rule.Rule;
import hous.server.domain.todo.Todo;
import hous.server.domain.user.Onboarding;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Column(nullable = false)
    private int rulesCnt;

    @Column(nullable = false)
    private int todosCnt;

    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Participate> participates = new ArrayList<>();

    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Rule> rules = new ArrayList<>();

    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Todo> todos = new ArrayList<>();

    @Builder(access = AccessLevel.PRIVATE)
    public Room(Onboarding owner, String name, String code, int participantsCnt, int rulesCnt, int todosCnt) {
        this.owner = owner;
        this.name = name;
        this.code = code;
        this.participantsCnt = participantsCnt;
        this.rulesCnt = rulesCnt;
        this.todosCnt = todosCnt;
    }

    public static Room newInstance(Onboarding owner, String name, String code) {
        return Room.builder()
                .owner(owner)
                .name(name)
                .code(code)
                .participantsCnt(0)
                .rulesCnt(0)
                .todosCnt(0)
                .build();
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

    public void addRule(Rule rule) {
        this.rules.add(rule);
        this.rulesCnt += 1;
    }

    public void deleteRule(Rule rule) {
        this.rules.remove(rule);
        this.rulesCnt -= 1;
    }

    public void addTodo(Todo todo) {
        this.todos.add(todo);
        this.todosCnt += 1;
    }

    public void deleteTodo(Todo todo) {
        this.todos.remove(todo);
        this.todosCnt -= 1;
    }
}
