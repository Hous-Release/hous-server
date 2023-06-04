package hous.core.domain.rule.mysql;

import org.springframework.data.jpa.repository.JpaRepository;

import hous.core.domain.rule.RuleImage;

public interface RuleImageRepository extends JpaRepository<RuleImage, Long>, RuleImageRepositoryCustom {
}
