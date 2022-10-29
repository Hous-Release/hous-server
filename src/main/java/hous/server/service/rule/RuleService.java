package hous.server.service.rule;

import hous.server.domain.badge.BadgeInfo;
import hous.server.domain.badge.repository.AcquireRepository;
import hous.server.domain.badge.repository.BadgeRepository;
import hous.server.domain.common.RedisKey;
import hous.server.domain.room.Room;
import hous.server.domain.rule.Rule;
import hous.server.domain.rule.repository.RuleRepository;
import hous.server.domain.user.Onboarding;
import hous.server.domain.user.User;
import hous.server.domain.user.repository.UserRepository;
import hous.server.service.badge.BadgeService;
import hous.server.service.badge.BadgeServiceUtils;
import hous.server.service.notification.NotificationService;
import hous.server.service.room.RoomServiceUtils;
import hous.server.service.rule.dto.request.CreateRuleRequestDto;
import hous.server.service.rule.dto.request.DeleteRuleReqeustDto;
import hous.server.service.rule.dto.request.UpdateRuleRequestDto;
import hous.server.service.user.UserServiceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Service
@Transactional
public class RuleService {

    private final RedisTemplate<String, Object> redisTemplate;

    private final UserRepository userRepository;
    private final RuleRepository ruleRepository;
    private final BadgeRepository badgeRepository;
    private final AcquireRepository acquireRepository;

    private final BadgeService badgeService;
    private final NotificationService notificationService;

    public void createRule(CreateRuleRequestDto request, Long userId) {
        User user = UserServiceUtils.findUserById(userRepository, userId);
        Onboarding me = user.getOnboarding();
        Room room = RoomServiceUtils.findParticipatingRoom(user);
        RuleServiceUtils.validateRuleCounts(room, request.getRuleNames().size());
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
            String createRuleCountString = (String) redisTemplate.opsForValue().get(RedisKey.CREATE_RULE_COUNT + userId);
            if (createRuleCountString != null && Integer.parseInt(createRuleCountString) >= 4) {
                badgeService.acquireBadge(user, BadgeInfo.OUR_HOUSE_PILLAR_HOMIE);
                redisTemplate.delete(RedisKey.CREATE_RULE_COUNT + user.getId());
            } else {
                if (createRuleCountString == null) {
                    redisTemplate.opsForValue().set(RedisKey.CREATE_RULE_COUNT + user.getId(), Integer.toString(1));
                } else {
                    redisTemplate.opsForValue().set(RedisKey.CREATE_RULE_COUNT + user.getId(), Integer.toString(Integer.parseInt(createRuleCountString) + 1));
                }
            }
        }
        List<User> usersExceptMe = RoomServiceUtils.findParticipatingUsersExceptMe(room, user);
        usersExceptMe.forEach(userExceptMe -> notificationService.sendNewRuleNotification(userExceptMe, rules));
    }

    public void updateRule(UpdateRuleRequestDto request, Long userId) {
        User user = UserServiceUtils.findUserById(userRepository, userId);
        Room room = RoomServiceUtils.findParticipatingRoom(user);
        RuleServiceUtils.validateRequestRuleCounts(room, request.getRules().size());
        IntStream.range(0, request.getRules().size()).forEach(idx -> {
                .mapToObj(idx -> {
            Rule rule = RuleServiceUtils.findRuleByIdAndRoom(ruleRepository, request.getRules().get(idx).getId(), room);
            rule.updateRule(request.getRules().get(idx).getName(), idx);
            room.updateRule(rule);
        });
        room.updateRules(rules);
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
