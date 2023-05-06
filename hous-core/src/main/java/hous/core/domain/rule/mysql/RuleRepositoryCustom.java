package hous.core.domain.rule.mysql;

import hous.core.domain.room.Room;
import hous.core.domain.rule.Rule;

public interface RuleRepositoryCustom {

	Rule findLastRuleByRoom(Room room);

	Rule findRuleByIdAndRoom(Long ruleId, Room room);
}
