package hous.server.domain.feedback.mysql;

import hous.server.domain.feedback.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback, Long>, FeedbackRepositoryCustom {
}
