package hous.server.service.rule;

import hous.server.common.exception.ConflictException;
import hous.server.common.exception.ForbiddenException;
import hous.server.common.exception.NotFoundException;
import hous.server.common.exception.ValidationException;
import hous.server.domain.common.Constraint;
import hous.server.domain.room.Room;
import hous.server.domain.rule.Rule;
import hous.server.domain.rule.repository.RuleRepository;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

import static hous.server.common.exception.ErrorCode.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RuleServiceUtils {
    public static int findRuleIdxByRoomId(RuleRepository ruleRepository, Room room) {
        Rule rule = ruleRepository.findLastRuleByRoom(room);
        if (rule == null) {
            return -1;
        }
        return rule.getIdx();
    }

    public static void validateRuleName(Room room, String ruleName) {
        if (ruleName.length() == 0) {
            throw new ValidationException(String.format("방 (%s) 의 ruleName (%s) 은 빈 값이 될 수 없습니다.", room.getId(), ruleName), VALIDATION_RULE_MIN_LENGTH_EXCEPTION);
        }
        if (ruleName.length() > Constraint.RULE_NAME_MAX) {
            throw new ValidationException(String.format("방 (%s) 의 ruleName (%s) 의 최대 길이는 20 글자 이내만 가능합니다.", room.getId(), ruleName), VALIDATION_RULE_MAX_LENGTH_EXCEPTION);
        }
    }

    public static void validateRuleCounts(Room room, int requestRuleCnt) {
        if (room.getRules().size() + requestRuleCnt > Constraint.RULE_COUNT_MAX) {
            throw new ForbiddenException(String.format("방 (%s) 의 rule 는 30 개를 초과할 수 없습니다.", room.getId()), FORBIDDEN_RULE_COUNT_EXCEPTION);
        }
    }

    public static Rule findRuleByIdAndRoom(RuleRepository ruleRepository, Long ruleId, Room room) {
        Rule rule = ruleRepository.findRuleByIdAndRoom(ruleId, room);
        if (rule == null) {
            throw new NotFoundException(String.format("존재하지 않는 규칙 (%s) 입니다", ruleId), NOT_FOUND_RULE_EXCEPTION);
        }
        return rule;
    }

    public static void existsRuleByRoomRules(Room room, List<String> requestRules) {
        List<String> rules = room.getRules().stream().map(Rule::getName).collect(Collectors.toList());
        for (String ruleName : requestRules) {
            if (rules.contains(ruleName)) {
                throw new ConflictException(String.format("방 (%s) 에 이미 존재하는 ruleName (%s) 입니다.", room.getId(), ruleName), CONFLICT_RULE_EXCEPTION);
            }
        }
    }
}
