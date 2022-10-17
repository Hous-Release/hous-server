package hous.server.domain.room;

import hous.server.domain.common.AuditingTimeEntity;
import hous.server.domain.user.Onboarding;
import hous.server.domain.user.TestScore;
import lombok.*;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class Participate extends AuditingTimeEntity implements Comparable<Participate> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "onboarding_id")
    private Onboarding onboarding;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    public static Participate newInstance(Onboarding onboarding, Room room) {
        return Participate.builder()
                .onboarding(onboarding)
                .room(room)
                .build();
    }

    @Override
    public int compareTo(@NotNull Participate o) {
        TestScore t1 = getOnboarding().getTestScore();
        TestScore t2 = o.getOnboarding().getTestScore();
        if (t1 == null && t2 != null) {
            return 1;
        }
        if (t1 != null && t2 == null) {
            return -1;
        }
        if (t1 == null && t2 == null) {
            return 0;
        }
        return t1.getCreatedAt().compareTo(t2.getCreatedAt());
    }
}
