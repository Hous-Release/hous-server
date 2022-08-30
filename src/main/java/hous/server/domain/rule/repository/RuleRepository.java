package hous.server.domain.rule.repository;

import hous.server.domain.rule.Rule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RuleRepository extends JpaRepository<Rule, Long>, RuleRepositoryCustom {
}
