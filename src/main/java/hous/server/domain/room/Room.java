package hous.server.domain.room;

import hous.server.domain.common.AuditingTimeEntity;
import hous.server.domain.rule.Rule;
import hous.server.domain.todo.Todo;
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
    public Room(String name, String code, int participantsCnt, int rulesCnt, int todosCnt) {
        this.name = name;
        this.code = code;
        this.participantsCnt = participantsCnt;
        this.rulesCnt = rulesCnt;
        this.todosCnt = todosCnt;
    }

    public static Room newInstance(String name, String code) {
        return Room.builder()
                .name(name)
                .code(code)
                .participantsCnt(0)
                .rulesCnt(0)
                .todosCnt(0)
                .build();
    }

    public void addParticipate(Participate participate) {
        this.participates.add(participate);
        this.participantsCnt += 1;
    }
}
