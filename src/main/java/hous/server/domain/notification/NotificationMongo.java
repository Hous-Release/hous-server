package hous.server.domain.notification;

import hous.server.domain.common.AuditingTimeEntity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Document
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class NotificationMongo extends AuditingTimeEntity {

    @Transient
    public static final String SEQUENCE_NAME = "notification_sequence";

    @Id
    @Setter
    private Long id;

    @Field
    private Long onboardingId;

    @Field
    private NotificationType type;

    @Field
    private String content;

    @Field
    private boolean isRead;

    public static NotificationMongo newInstance(Long onboardingId, NotificationType type, String content, boolean isRead) {
        return NotificationMongo.builder()
                .onboardingId(onboardingId)
                .type(type)
                .content(content)
                .isRead(isRead)
                .build();
    }

    public void updateIsRead() {
        this.isRead = true;
    }
}
