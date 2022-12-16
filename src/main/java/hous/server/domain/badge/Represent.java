package hous.server.domain.badge;

import hous.server.domain.common.AuditingTimeEntity;
import hous.server.domain.user.Onboarding;
import lombok.*;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class Represent extends AuditingTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "onboarding_id")
    private Onboarding onboarding;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "badge_id")
    private Badge badge;

    public static Represent newInstance(Onboarding onboarding, Badge badge) {
        return Represent.builder()
                .onboarding(onboarding)
                .badge(badge)
                .build();
    }
}
