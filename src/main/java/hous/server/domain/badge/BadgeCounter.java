package hous.server.domain.badge;

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
public class BadgeCounter extends AuditingTimeEntity {

    @Transient
    public static final String SEQUENCE_NAME = "badge_counter_sequence";

    @Id
    @Setter
    private Long id;

    @Field
    private Long userId;

    @Field
    private int ruleCreateCount;

    @Field
    private int todoCompleteCount;

    @Field
    private int testScoreCompleteCount;

    public static BadgeCounter newInstance(Long userId) {
        return BadgeCounter.builder()
                .userId(userId)
                .ruleCreateCount(0)
                .todoCompleteCount(0)
                .testScoreCompleteCount(0)
                .build();
    }
}
