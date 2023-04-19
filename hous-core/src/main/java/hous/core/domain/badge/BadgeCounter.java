package hous.core.domain.badge;

import hous.core.domain.common.AuditingTimeEntity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Document(collection = "badge_counter")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class BadgeCounter extends AuditingTimeEntity {

    @Transient
    public static final String SEQUENCE_NAME = "badge_counter_sequence";

    @Id
    @Setter
    private Long id;

    @Field(name = "user_id")
    private Long userId;

    @Field(name = "count_type")
    private BadgeCounterType countType;

    @Field(name = "count")
    private int count;

    public static BadgeCounter newInstance(Long userId, BadgeCounterType countType, int count) {
        return builder()
                .userId(userId)
                .countType(countType)
                .count(count)
                .build();
    }

    public void updateCount(int count) {
        this.count = count;
    }
}
