package hous.server.domain.room;

import hous.server.domain.common.AuditingTimeEntity;
import hous.server.domain.user.Onboarding;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Participate extends AuditingTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "onboarding_id")
    private Onboarding onboarding;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    @Builder(access = AccessLevel.PRIVATE)
    public Participate(Onboarding onboarding, Room room) {
        this.onboarding = onboarding;
        this.room = room;
    }

    public static Participate newInstance(Onboarding onboarding, Room room) {
        return Participate.builder()
                .onboarding(onboarding)
                .room(room)
                .build();
    }
}
