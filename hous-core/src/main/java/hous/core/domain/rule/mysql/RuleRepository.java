package hous.core.domain.rule.mysql;

import hous.core.domain.rule.Rule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RuleRepository extends JpaRepository<Rule, Long>, RuleRepositoryCustom {
}
