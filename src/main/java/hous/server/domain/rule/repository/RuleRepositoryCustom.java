package hous.server.domain.rule.repository;

import hous.server.domain.room.Room;
import hous.server.domain.rule.Rule;

public interface RuleRepositoryCustom {

    Rule findLastRuleByRoomId(Room room);
}
