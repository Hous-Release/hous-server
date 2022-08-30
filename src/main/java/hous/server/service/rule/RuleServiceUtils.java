package hous.server.service.rule;

import hous.server.common.exception.ForbiddenException;
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

    public static int findRuleIdxByRoomId(RuleRepository ruleRepository, Long roomId) {
        Rule rule = ruleRepository.findRuleIdxByRoomId(roomId);
        if (rule == null) {
            return 0;
        }
        return rule.getIdx();
    }

    public static boolean validateRuleCounts(RuleRepository ruleRepository, Long roomId) {
        if (ruleRepository.validateRuleCountsByRoomId(roomId) >= 30) {
            throw new ForbiddenException(String.format("방 (%s) 의 todo 는 60개를 초과할 수 없습니다.", roomId), FORBIDDEN_RULE_COUNT_EXCEPTION);
        }
        return true;
    }
}
