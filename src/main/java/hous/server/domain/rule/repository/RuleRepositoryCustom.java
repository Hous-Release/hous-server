package hous.server.domain.rule.repository;

import hous.server.domain.room.Room;
import hous.server.domain.rule.Rule;

public interface RuleRepositoryCustom {

    Rule findLastRuleByRoom(Room room);

    Rule findRuleByIdAndRoom(Long ruleId, Room room);
}
