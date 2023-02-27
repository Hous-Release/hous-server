package hous.server.domain.user.mysql;

import hous.server.domain.user.TestScore;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestScoreRepository extends JpaRepository<TestScore, Long>, TestScoreRepositoryCustom {
}
