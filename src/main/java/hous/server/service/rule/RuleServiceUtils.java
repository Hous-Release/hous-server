package hous.server.service.rule;

import hous.server.common.exception.ForbiddenException;
import hous.server.domain.room.Room;
import hous.server.domain.rule.Rule;
import hous.server.domain.rule.repository.RuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static hous.server.common.exception.ErrorCode.FORBIDDEN_RULE_COUNT_EXCEPTION;

@RequiredArgsConstructor
@Service
@Transactional
public class RuleServiceUtils {
    public static int findRuleIdxByRoomId(RuleRepository ruleRepository, Room room) {
        Rule rule = ruleRepository.findLastRuleByRoomId(room);
        if (rule == null) {
            return 0;
        }
        return rule.getIdx();
    }

    public static void validateRuleCounts(Room room) {
        if (room.getRulesCnt() >= 30) {
            throw new ForbiddenException(String.format("방 (%s) 의 rule 는 30개를 초과할 수 없습니다.", room.getId()), FORBIDDEN_RULE_COUNT_EXCEPTION);
        }
    }
}
