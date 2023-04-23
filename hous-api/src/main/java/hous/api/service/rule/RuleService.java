package hous.api.service.rule;

import hous.api.service.badge.BadgeService;
import hous.api.service.badge.BadgeServiceUtils;
import hous.api.service.notification.NotificationService;
import hous.api.service.room.RoomServiceUtils;
import hous.api.service.rule.dto.request.CreateRuleRequestDto;
import hous.api.service.rule.dto.request.DeleteRuleReqeustDto;
import hous.api.service.rule.dto.request.UpdateRuleRequestDto;
import hous.api.service.rule.dto.response.RuleInfo;
import hous.api.service.user.UserServiceUtils;
import hous.core.domain.badge.BadgeCounter;
import hous.core.domain.badge.BadgeCounterType;
import hous.core.domain.badge.BadgeInfo;
import hous.core.domain.badge.mongo.BadgeCounterRepository;
import hous.core.domain.badge.mysql.AcquireRepository;
import hous.core.domain.badge.mysql.BadgeRepository;
import hous.core.domain.room.Room;
import hous.core.domain.rule.Rule;
import hous.core.domain.rule.mysql.RuleRepository;
import hous.core.domain.user.Onboarding;
import hous.core.domain.user.User;
import hous.core.domain.user.mysql.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class RuleService {

    private final RedisTemplate<String, Object> redisTemplate;

    private final UserRepository userRepository;
    private final RuleRepository ruleRepository;
    private final BadgeRepository badgeRepository;
    private final AcquireRepository acquireRepository;
    private final BadgeCounterRepository badgeCounterRepository;

    private final BadgeService badgeService;
    private final NotificationService notificationService;

    public void createRule(CreateRuleRequestDto request, Long userId) {
        User user = UserServiceUtils.findUserById(userRepository, userId);
        Onboarding me = user.getOnboarding();
        Room room = RoomServiceUtils.findParticipatingRoom(user);
        RuleServiceUtils.validateRuleCounts(room, request.getRuleNames().size());
        RuleServiceUtils.existsRuleByRoomRules(room, request.getRuleNames());
        AtomicInteger ruleIdx = new AtomicInteger(RuleServiceUtils.findRuleIdxByRoomId(ruleRepository, room));
        List<Rule> rules = request.getRuleNames().stream()
                .map(ruleName -> {
                    RuleServiceUtils.validateRuleName(room, ruleName);
                    return ruleRepository.save(Rule.newInstance(room, ruleName, ruleIdx.addAndGet(1)));
                })
                .collect(Collectors.toList());
        room.addRules(rules);

        badgeService.acquireBadge(user, BadgeInfo.LETS_BUILD_A_POLE);
        if (!BadgeServiceUtils.hasBadge(badgeRepository, acquireRepository, BadgeInfo.OUR_HOUSE_PILLAR_HOMIE, me)) {
            BadgeCounter ruleComplete = badgeCounterRepository.findByUserIdAndCountType(userId, BadgeCounterType.RULE_CREATE);
            if (ruleComplete != null && ruleComplete.getCount() >= 4) {
                badgeService.acquireBadge(user, BadgeInfo.OUR_HOUSE_PILLAR_HOMIE);
                badgeCounterRepository.deleteBadgeCounterByUserIdAndCountType(userId, BadgeCounterType.RULE_CREATE);
            } else {
                if (ruleComplete == null) {
                    badgeCounterRepository.save(BadgeCounter.newInstance(userId, BadgeCounterType.RULE_CREATE, 1));
                } else {
                    ruleComplete.updateCount(ruleComplete.getCount() + 1);
                    badgeCounterRepository.save(ruleComplete);
                }
            }
        }

        List<User> usersExceptMe = RoomServiceUtils.findParticipatingUsersExceptMe(room, user);
        usersExceptMe.forEach(userExceptMe -> notificationService.sendNewRuleNotification(userExceptMe, rules));
    }

    public void updateRules(UpdateRuleRequestDto request, Long userId) {
        User user = UserServiceUtils.findUserById(userRepository, userId);
        Room room = RoomServiceUtils.findParticipatingRoom(user);
        RuleServiceUtils.existsRuleByRules(request.getRules().stream().map(RuleInfo::getName).collect(Collectors.toList()));
        for (int idx = 0; idx < request.getRules().size(); idx++) {
            Rule rule = RuleServiceUtils.findRuleByIdAndRoom(ruleRepository, request.getRules().get(idx).getId(), room);
            RuleServiceUtils.validateRuleName(room, request.getRules().get(idx).getName());
            rule.updateRule(request.getRules().get(idx).getName(), idx);
            room.updateRule(rule);
        }
    }

    public void deleteRules(DeleteRuleReqeustDto request, Long userId) {
        User user = UserServiceUtils.findUserById(userRepository, userId);
        Room room = RoomServiceUtils.findParticipatingRoom(user);
        request.getRulesIdList().forEach(ruleId -> {
            Rule rule = RuleServiceUtils.findRuleByIdAndRoom(ruleRepository, ruleId, room);
            room.deleteRule(rule);
            ruleRepository.delete(rule);
        });

    }
}
