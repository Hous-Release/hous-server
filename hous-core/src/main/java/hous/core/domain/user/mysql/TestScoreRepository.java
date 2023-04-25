package hous.core.domain.user.mysql;

import org.springframework.data.jpa.repository.JpaRepository;

import hous.core.domain.user.TestScore;

public interface TestScoreRepository extends JpaRepository<TestScore, Long>, TestScoreRepositoryCustom {
}
