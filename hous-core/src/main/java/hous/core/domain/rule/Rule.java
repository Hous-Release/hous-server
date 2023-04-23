package hous.core.domain.rule;

import hous.core.domain.common.AuditingTimeEntity;
import hous.core.domain.room.Room;
import lombok.*;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;

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
    public int compareTo(@NotNull Rule o) {
        if (idx == o.idx) {
            if (o.getCreatedAt().compareTo(getCreatedAt()) == 0) {
                return Long.compare(id, o.id);
            }
            return getCreatedAt().compareTo(o.getCreatedAt());
        }
        return Integer.compare(idx, o.idx);
    }
}
