package hous.server.service.rule;

import hous.server.common.exception.ForbiddenException;
import hous.server.common.exception.NotFoundException;
import hous.server.domain.room.Room;
import hous.server.domain.rule.Rule;
import hous.server.domain.rule.repository.RuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static hous.server.common.exception.ErrorCode.*;

@RequiredArgsConstructor
@Service
@Transactional
public class RuleServiceUtils {
    public static int findRuleIdxByRoomId(RuleRepository ruleRepository, Room room) {
        Rule rule = ruleRepository.findLastRuleByRoom(room);
        if (rule == null) {
            return -1;
        }
        return rule.getIdx();
    }

    public static void validateRuleCounts(Room room) {
        if (room.getRulesCnt() >= 30) {
            throw new ForbiddenException(String.format("방 (%s) 의 rule 는 30개를 초과할 수 없습니다.", room.getId()), FORBIDDEN_RULE_COUNT_EXCEPTION);
        }
    }

    public static Rule findRuleByIdAndRoom(RuleRepository ruleRepository, Long ruleId, Room room) {
        Rule rule = ruleRepository.findRuleByIdAndRoom(ruleId, room);
        if (rule == null) {
            throw new NotFoundException(String.format("존재하지 않는 규칙 (%s) 입니다", ruleId), NOT_FOUND_RULE_EXCEPTION);
        }
        return rule;
    }

    public static void validateReqeustRuleCounts(Room room, int requstRuleCounts) {
        if (room.getRulesCnt() != requstRuleCounts) {
            throw new NotFoundException(String.format("방 (%s) 의 rule 는 (%s) 개가 아닙니다.", room.getId(), requstRuleCounts),
                    FORBIDDEN_REQUEST_RULE_COUNT_EXCEPTION);
        }
    }

}
