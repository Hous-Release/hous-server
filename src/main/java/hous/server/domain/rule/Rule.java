package hous.server.domain.rule;

import hous.server.domain.common.AuditingTimeEntity;
import hous.server.domain.room.Room;
import lombok.*;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class Rule extends AuditingTimeEntity {

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
        return Rule.builder()
                .room(room)
                .name(name)
                .idx(idx)
                .build();
    }

    public void updateRule(String name, int idx) {
        this.idx = idx;
        if (this.name.equals(name)) {
            return;
        }
        this.name = name;
    }

}
