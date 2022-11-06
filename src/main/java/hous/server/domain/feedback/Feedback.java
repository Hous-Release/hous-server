package hous.server.domain.feedback;

import hous.server.domain.common.AuditingTimeEntity;
import lombok.*;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class Feedback extends AuditingTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private FeedbackType feedbackType;

    @Column(length = 300)
    private String comment;

    public static Feedback newInstance(FeedbackType feedbackType, String comment) {
        return Feedback.builder()
                .feedbackType(feedbackType)
                .comment(comment)
                .build();
    }
}
