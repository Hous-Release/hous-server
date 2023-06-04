package hous.api.service.rule;

import static hous.common.exception.ErrorCode.*;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import hous.api.service.badge.BadgeService;
import hous.api.service.badge.BadgeServiceUtils;
import hous.api.service.image.provider.S3Provider;
import hous.api.service.image.provider.dto.request.ImageUploadFileRequest;
import hous.api.service.notification.NotificationService;
import hous.api.service.room.RoomServiceUtils;
import hous.api.service.rule.dto.request.CreateRuleInfoRequestDto;
import hous.api.service.rule.dto.request.CreateRuleRequestDto;
import hous.api.service.rule.dto.request.DeleteRuleRequestDto;
import hous.api.service.rule.dto.request.UpdateRuleRequestDto;
import hous.api.service.rule.dto.response.RuleInfo;
import hous.api.service.user.UserServiceUtils;
import hous.common.constant.Constraint;
import hous.common.exception.ValidationException;
import hous.common.type.FileType;
import hous.core.domain.badge.BadgeCounter;
import hous.core.domain.badge.BadgeCounterType;
import hous.core.domain.badge.BadgeInfo;
import hous.core.domain.badge.mongo.BadgeCounterRepository;
import hous.core.domain.badge.mysql.AcquireRepository;
import hous.core.domain.badge.mysql.BadgeRepository;
import hous.core.domain.room.Room;
import hous.core.domain.rule.Rule;
import hous.core.domain.rule.RuleImage;
import hous.core.domain.rule.mysql.RuleImageRepository;
import hous.core.domain.rule.mysql.RuleRepository;
import hous.core.domain.user.Onboarding;
import hous.core.domain.user.User;
import hous.core.domain.user.mysql.UserRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional
public class RuleService {

	private final RedisTemplate<String, Object> redisTemplate;

	private final UserRepository userRepository;
	private final RuleRepository ruleRepository;
	private final RuleImageRepository ruleImageRepository;
	private final BadgeRepository badgeRepository;
	private final AcquireRepository acquireRepository;
	private final BadgeCounterRepository badgeCounterRepository;

	private final BadgeService badgeService;
	private final NotificationService notificationService;

	private final S3Provider s3Provider;

	// TODO Deprecated
	public void createRuleDeprecated(CreateRuleRequestDto request, Long userId) {
		User user = UserServiceUtils.findUserById(userRepository, userId);
		Onboarding me = user.getOnboarding();
		Room room = RoomServiceUtils.findParticipatingRoom(user);
		RuleServiceUtils.validateRuleCounts(room, request.getRuleNames().size());
		RuleServiceUtils.existsRuleByRoomRules(room, request.getRuleNames());
		AtomicInteger ruleIdx = new AtomicInteger(RuleServiceUtils.findRuleIdxByRoomId(ruleRepository, room));
		List<Rule> rules = request.getRuleNames().stream()
			.map(ruleName -> {
				RuleServiceUtils.validateRuleName(room, ruleName);
				return ruleRepository.save(Rule.newInstance(room, ruleName, ruleIdx.addAndGet(1), ""));
			})
			.collect(Collectors.toList());
		room.addRules(rules);

		badgeService.acquireBadge(user, BadgeInfo.LETS_BUILD_A_POLE);
		if (!BadgeServiceUtils.hasBadge(badgeRepository, acquireRepository, BadgeInfo.OUR_HOUSE_PILLAR_HOMIE, me)) {
			BadgeCounter ruleComplete = badgeCounterRepository.findByUserIdAndCountType(userId,
				BadgeCounterType.RULE_CREATE);
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

	public void createRule(CreateRuleInfoRequestDto request, Long userId, List<MultipartFile> images) {
		User user = UserServiceUtils.findUserById(userRepository, userId);
		Onboarding me = user.getOnboarding();
		Room room = RoomServiceUtils.findParticipatingRoom(user);
		RuleServiceUtils.existsNowRuleByRoomRule(room, request.getName());
		if (images.size() > Constraint.RULE_IMAGE_MAX) {
			throw new ValidationException(
				String.format("방 (%s) 의 규칙 이미지는 최대 % 개만 가능합니다.", room.getId(), Constraint.RULE_IMAGE_MAX),
				VALIDATION_RULE_IMAGE_MAX_COUNT_EXCEPTION);
		}
		Rule rule = Rule.newInstance(room, request.getName(), 0, request.getDescription());
		ruleRepository.save(rule);
		List<RuleImage> s3ImageUrls = images.stream().map(image -> {
			String imageUrl = s3Provider.uploadFile(ImageUploadFileRequest.of(FileType.IMAGE), image);
			return ruleImageRepository.save(RuleImage.newInstance(rule, image.toString(), imageUrl));
		}).collect(Collectors.toList());
		rule.addAllRuleImage(s3ImageUrls);
		room.addRule(rule);

		badgeService.acquireBadge(user, BadgeInfo.LETS_BUILD_A_POLE);
		if (!BadgeServiceUtils.hasBadge(badgeRepository, acquireRepository, BadgeInfo.OUR_HOUSE_PILLAR_HOMIE, me)) {
			BadgeCounter ruleComplete = badgeCounterRepository.findByUserIdAndCountType(userId,
				BadgeCounterType.RULE_CREATE);
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
		usersExceptMe.forEach(userExceptMe -> notificationService.sendNewRuleNotification(userExceptMe, rule));
	}

	public void updateRules(UpdateRuleRequestDto request, Long userId) {
		User user = UserServiceUtils.findUserById(userRepository, userId);
		Room room = RoomServiceUtils.findParticipatingRoom(user);
		RuleServiceUtils.existsRuleByRules(
			request.getRules().stream().map(RuleInfo::getName).collect(Collectors.toList()));
		for (int idx = 0; idx < request.getRules().size(); idx++) {
			Rule rule = RuleServiceUtils.findRuleByIdAndRoom(ruleRepository, request.getRules().get(idx).getId(), room);
			RuleServiceUtils.validateRuleName(room, request.getRules().get(idx).getName());
			rule.updateRule(request.getRules().get(idx).getName(), idx);
			room.updateRule(rule);
		}
	}

	public void deleteRules(DeleteRuleRequestDto request, Long userId) {
		User user = UserServiceUtils.findUserById(userRepository, userId);
		Room room = RoomServiceUtils.findParticipatingRoom(user);
		request.getRulesIdList().forEach(ruleId -> {
			Rule rule = RuleServiceUtils.findRuleByIdAndRoom(ruleRepository, ruleId, room);
			room.deleteRule(rule);
			ruleRepository.delete(rule);
		});

	}
}
