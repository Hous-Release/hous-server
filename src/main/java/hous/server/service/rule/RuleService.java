package hous.server.service.rule;

import hous.server.domain.room.Room;
import hous.server.domain.rule.Rule;
import hous.server.domain.rule.repository.RuleRepository;
import hous.server.domain.user.User;
import hous.server.domain.user.repository.UserRepository;
import hous.server.service.room.RoomServiceUtils;
import hous.server.service.rule.dto.request.CreateRuleRequestDto;
import hous.server.service.rule.dto.request.UpdateRuleRequestDto;
import hous.server.service.rule.dto.request.UpdateSortByRuleRequestDto;
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

    public void createRule(CreateRuleRequestDto request, Long userId) {
        User user = UserServiceUtils.findUserById(userRepository, userId);
        Room room = RoomServiceUtils.findParticipatingRoom(user);
        RuleServiceUtils.validateRuleCounts(room);
        int ruleIdx = RuleServiceUtils.findRuleIdxByRoomId(ruleRepository, room);
        Rule rule = ruleRepository.save(Rule.newInstance(room, request.getName(), ruleIdx + 1));
        room.addRule(rule);
    }

    public void updateRule(UpdateRuleRequestDto request, Long ruleId, Long userId) {
        User user = UserServiceUtils.findUserById(userRepository, userId);
        Room room = RoomServiceUtils.findParticipatingRoom(user);
        Rule rule = RuleServiceUtils.findRuleByIdAndRoom(ruleRepository, ruleId, room);
        rule.updateRuleName(request);
    }

    public void updateSortByRule(UpdateSortByRuleRequestDto request, Long userId) {
        User user = UserServiceUtils.findUserById(userRepository, userId);
        Room room = RoomServiceUtils.findParticipatingRoom(user);
        RuleServiceUtils.validateReqeustRuleCounts(room, request.getUpdateRuleIds().size());
        for (int idx = 0; idx < request.getUpdateRuleIds().size(); idx++) {
            Long ruleId = request.getUpdateRuleIds().get(idx);
            Rule rule = RuleServiceUtils.findRuleByIdAndRoom(ruleRepository, ruleId, room);
            rule.updateRuleIndex(idx);
        }
    }
}
