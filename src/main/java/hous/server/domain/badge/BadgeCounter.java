package hous.server.domain.badge;

import hous.server.domain.common.AuditingTimeEntity;
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

    @Field(name = "rule_create_count")
    private int ruleCreateCount;

    @Field(name = "todo_complete_count")
    private int todoCompleteCount;

    @Field(name = "test_score_complete_count")
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
