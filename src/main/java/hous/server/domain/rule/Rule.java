package hous.server.domain.rule;

import hous.server.domain.common.AuditingTimeEntity;
import hous.server.domain.room.Room;
import hous.server.service.rule.dto.request.UpdateRuleRequestDto;
import lombok.*;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
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

    public void updateRuleName(UpdateRuleRequestDto request) {
        this.name = request.getName();
    }

    public void updateRuleIndex(int ruleIdx) {
        this.idx = ruleIdx;
    }

}
