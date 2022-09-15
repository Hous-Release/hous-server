package hous.server.service.rule;

import hous.server.domain.badge.Acquire;
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
import hous.server.service.badge.BadgeServiceUtils;
import hous.server.service.notification.NotificationService;
import hous.server.service.room.RoomServiceUtils;
import hous.server.service.rule.dto.request.CreateRuleRequestDto;
import hous.server.service.rule.dto.request.ModifyRuleReqeustDto;
import hous.server.service.rule.dto.request.UpdateRuleRequestDto;
import hous.server.service.user.UserServiceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class RuleService {

    private final RedisTemplate<String, Object> redisTemplate;

    private final UserRepository userRepository;
    private final RuleRepository ruleRepository;
    private final BadgeRepository badgeRepository;
    private final AcquireRepository acquireRepository;

    private final NotificationService notificationService;

    public void createRule(CreateRuleRequestDto request, Long userId) {
        User user = UserServiceUtils.findUserById(userRepository, userId);
        Onboarding me = user.getOnboarding();
        Room room = RoomServiceUtils.findParticipatingRoom(user);
        RuleServiceUtils.validateRuleCounts(room);
        int ruleIdx = RuleServiceUtils.findRuleIdxByRoomId(ruleRepository, room);
        Rule rule = ruleRepository.save(Rule.newInstance(room, request.getName(), ruleIdx + 1));
        room.addRule(rule);

        if (!BadgeServiceUtils.hasBadge(badgeRepository, acquireRepository, BadgeInfo.LETS_BUILD_A_POLE, me)) {
            Acquire acquire = acquireRepository.save(Acquire.newInstance(me, badgeRepository.findBadgeByBadgeInfo(BadgeInfo.LETS_BUILD_A_POLE)));
            me.addAcquire(acquire);
            notificationService.sendNewBadgeNotification(user, BadgeInfo.LETS_BUILD_A_POLE);
        }

        if (!BadgeServiceUtils.hasBadge(badgeRepository, acquireRepository, BadgeInfo.OUR_HOUSE_PILLAR_HOMIE, me)) {
            String createRuleCountString = (String) redisTemplate.opsForValue().get(RedisKey.CREATE_RULE_COUNT + userId);
            if (createRuleCountString != null && Integer.parseInt(createRuleCountString) >= 4) {
                Acquire acquire = acquireRepository.save(Acquire.newInstance(me, badgeRepository.findBadgeByBadgeInfo(BadgeInfo.OUR_HOUSE_PILLAR_HOMIE)));
                me.addAcquire(acquire);
                notificationService.sendNewBadgeNotification(user, BadgeInfo.OUR_HOUSE_PILLAR_HOMIE);
                redisTemplate.delete(RedisKey.CREATE_RULE_COUNT + user.getId());
            } else {
                if (createRuleCountString == null) {
                    redisTemplate.opsForValue().set(RedisKey.CREATE_RULE_COUNT + user.getId(), Integer.toString(1));
                } else {
                    redisTemplate.opsForValue().set(RedisKey.CREATE_RULE_COUNT + user.getId(), Integer.toString(Integer.parseInt(createRuleCountString) + 1));
                }
            }
        }
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
