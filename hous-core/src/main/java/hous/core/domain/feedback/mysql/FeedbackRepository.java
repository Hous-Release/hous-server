package hous.core.domain.feedback.mysql;

import org.springframework.data.jpa.repository.JpaRepository;

import hous.core.domain.feedback.Feedback;

public interface FeedbackRepository extends JpaRepository<Feedback, Long>, FeedbackRepositoryCustom {
}
