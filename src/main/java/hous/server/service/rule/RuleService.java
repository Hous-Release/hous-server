package hous.server.service.rule;

import hous.server.domain.room.Room;
import hous.server.domain.rule.Rule;
import hous.server.domain.rule.repository.RuleRepository;
import hous.server.domain.user.User;
import hous.server.domain.user.repository.UserRepository;
import hous.server.service.room.RoomServiceUtils;
import hous.server.service.rule.dto.request.CreateRuleRequestDto;
import hous.server.service.rule.dto.request.ModifyRuleReqeustDto;
import hous.server.service.rule.dto.request.UpdateRuleRequestDto;
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

    public void updateSortByRule(ModifyRuleReqeustDto request, Long userId) {
        User user = UserServiceUtils.findUserById(userRepository, userId);
        Room room = RoomServiceUtils.findParticipatingRoom(user);
        RuleServiceUtils.validateRequestRuleCounts(room, request.getRulesIdList().size());
        for (int idx = 0; idx < request.getRulesIdList().size(); idx++) {
            Long ruleId = request.getRulesIdList().get(idx);
            Rule rule = RuleServiceUtils.findRuleByIdAndRoom(ruleRepository, ruleId, room);
            rule.updateRuleIndex(idx);
        }
    }

    public void deleteRules(ModifyRuleReqeustDto request, Long userId) {
        User user = UserServiceUtils.findUserById(userRepository, userId);
        Room room = RoomServiceUtils.findParticipatingRoom(user);
        request.getRulesIdList().forEach(ruleId -> {
            Rule rule = RuleServiceUtils.findRuleByIdAndRoom(ruleRepository, ruleId, room);
            room.deleteRule(rule);
            ruleRepository.delete(rule);
        });

    }
}
