package hous.core.domain.user.mysql;

import hous.core.domain.user.TestScore;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestScoreRepository extends JpaRepository<TestScore, Long>, TestScoreRepositoryCustom {
}
