package hous.server.domain.rule.repository;

import hous.server.domain.rule.Rule;

import java.util.List;

public interface RuleRepositoryCustom {

    Rule findRuleIdxByRoomId(Long id);

    int validateRuleCountsByRoomId(Long id);

    List<Rule> findRulesByRoomId(Long id);
}
