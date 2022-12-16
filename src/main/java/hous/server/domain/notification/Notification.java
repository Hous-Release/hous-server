package hous.server.domain.notification;

import hous.server.domain.common.AuditingTimeEntity;
import hous.server.domain.user.Onboarding;
import lombok.*;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class Notification extends AuditingTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "onboarding_id")
    private Onboarding onboarding;

    @Column(nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @Column(nullable = false, length = 100)
    private String content;

    @Column(nullable = false)
    private boolean isRead;

    public static Notification newInstance(Onboarding onboarding, NotificationType type, String content, boolean isRead) {
        return Notification.builder()
                .onboarding(onboarding)
                .type(type)
                .content(content)
                .isRead(isRead)
                .build();
    }

    public void updateIsRead() {
        this.isRead = true;
    }
}
