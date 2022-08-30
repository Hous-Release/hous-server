package hous.server.service.rule;

import hous.server.domain.room.Room;
import hous.server.domain.rule.Rule;
import hous.server.domain.rule.repository.RuleRepository;
import hous.server.domain.user.User;
import hous.server.domain.user.repository.UserRepository;
import hous.server.service.room.RoomServiceUtils;
import hous.server.service.rule.dto.request.CreateRuleRequestDto;
import hous.server.service.rule.dto.response.RuleInfoResponse;
import hous.server.service.user.UserServiceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class RuleService {

    private final UserRepository userRepository;
    private final RuleRepository ruleRepository;

    public RuleInfoResponse createRule(CreateRuleRequestDto request, Long userId) {
        User user = UserServiceUtils.findUserById(userRepository, userId);
        Room room = RoomServiceUtils.findParticipatingRoom(user);
        RuleServiceUtils.validateRuleCounts(ruleRepository, room.getId());
        int ruleIdx = RuleServiceUtils.findRuleIdxByRoomId(ruleRepository, room.getId());
        Rule rule = ruleRepository.save(Rule.newInstance(room, request.getName(), ruleIdx + 1));
        return RuleInfoResponse.of(rule);
    }
}
