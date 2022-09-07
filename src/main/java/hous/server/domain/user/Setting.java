package hous.server.domain.user;

import hous.server.domain.common.AuditingTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Setting extends AuditingTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private boolean isPushNotification;

    @Builder(access = AccessLevel.PRIVATE)
    public Setting(boolean isPushNotification) {
        this.isPushNotification = isPushNotification;
    }

    public static Setting newInstance() {
        return Setting.builder()
                .isPushNotification(true)
                .build();
    }

    public void setPushNotification(boolean isPushNotification) {
        this.isPushNotification = isPushNotification;
    }
}
