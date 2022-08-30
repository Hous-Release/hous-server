package hous.server.domain.rule.repository;

import hous.server.domain.rule.Rule;

public interface RuleRepositoryCustom {

    Rule findRuleIdxByRoomId(Long id);

    int validateRuleCountsByRoomId(Long id);
}
