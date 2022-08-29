package hous.server.domain.todo;

import hous.server.domain.common.AuditingTimeEntity;
import hous.server.domain.room.Room;
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

    @Builder(access = AccessLevel.PRIVATE)
    public Todo(Room room, String name, boolean isPushNotification) {
        this.room = room;
        this.name = name;
        this.isPushNotification = isPushNotification;
    }

    public static Todo newInstance(Room room, String name, boolean isPushNotification) {
        return Todo.builder()
                .room(room)
                .name(name)
                .isPushNotification(isPushNotification)
                .build();
    }

    public void addTake(Take take) {
        this.takes.add(take);
    }
}
