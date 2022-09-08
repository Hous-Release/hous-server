package hous.server.domain.todo;

import hous.server.domain.common.AuditingTimeEntity;
import hous.server.domain.room.Room;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
        return Todo.builder()
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
