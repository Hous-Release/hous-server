package hous.server.domain.user;

import hous.server.domain.common.AuditingTimeEntity;
import lombok.*;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class Setting extends AuditingTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private boolean isPushNotification;

    public static Setting newInstance() {
        return Setting.builder()
                .isPushNotification(true)
                .build();
    }

    public void setPushNotification(boolean isPushNotification) {
        this.isPushNotification = isPushNotification;
    }
}
