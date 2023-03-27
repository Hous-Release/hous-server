package hous.server.domain.notification;

import hous.server.domain.common.AuditingTimeEntity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Getter
@Document(collection = "notification")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class Notification extends AuditingTimeEntity {

    @Transient
    public static final String SEQUENCE_NAME = "notification_sequence";

    @Id
    @Setter
    private Long id;

    @Field(name = "onboarding_id")
    private Long onboardingId;

    @Field(name = "type")
    private NotificationType type;

    @Field(name = "content")
    private String content;

    @Field(name = "is_read")
    private boolean isRead;

    @Indexed(name = "notification_ttl", expireAfterSeconds = 60 * 60 * 24 * 30)
    private LocalDateTime expireAt;

    public static Notification newInstance(Long onboardingId, NotificationType type, String content, boolean isRead) {
        return Notification.builder()
                .onboardingId(onboardingId)
                .type(type)
                .content(content)
                .isRead(isRead)
                .expireAt(LocalDateTime.now())
                .build();
    }

    public void updateIsRead() {
        this.isRead = true;
    }
}
