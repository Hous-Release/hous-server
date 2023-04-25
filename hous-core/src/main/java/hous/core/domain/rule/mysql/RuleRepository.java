package hous.core.domain.rule.mysql;

import org.springframework.data.jpa.repository.JpaRepository;

import hous.core.domain.rule.Rule;

public interface RuleRepository extends JpaRepository<Rule, Long>, RuleRepositoryCustom {
}
