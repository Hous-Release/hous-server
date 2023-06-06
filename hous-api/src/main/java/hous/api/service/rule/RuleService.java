package hous.api.service.rule;

import static hous.common.exception.ErrorCode.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import hous.api.service.badge.BadgeService;
import hous.api.service.badge.BadgeServiceUtils;
import hous.api.service.image.dto.UploadResponseDto;
import hous.api.service.image.provider.S3Provider;
import hous.api.service.image.provider.dto.request.ImageUploadFileRequest;
import hous.api.service.notification.NotificationService;
import hous.api.service.room.RoomServiceUtils;
import hous.api.service.rule.dto.request.CreateRuleInfoRequestDto;
import hous.api.service.rule.dto.request.CreateRuleRequestDto;
import hous.api.service.rule.dto.request.DeleteRuleRequestDto;
import hous.api.service.rule.dto.request.UpdateRuleInfoRequestDto;
import hous.api.service.rule.dto.request.UpdateRuleRepresentRequestDto;
import hous.api.service.rule.dto.request.UpdateRuleRequestDto;
import hous.api.service.rule.dto.response.RuleInfo;
import hous.api.service.user.UserServiceUtils;
import hous.common.exception.ConflictException;
import hous.common.exception.ForbiddenException;
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
		var maybeImages = Optional.ofNullable(images);
		maybeImages.ifPresent(imageList -> RuleServiceUtils.validateRuleImageFileCounts(room, imageList));
		Rule rule = Rule.newInstance(room, request.getName(), 0, request.getDescription());
		ruleRepository.save(rule);
		List<RuleImage> s3ImageUrls = maybeImages.orElse(Collections.emptyList()).stream().map(image -> {
			UploadResponseDto response = s3Provider.uploadFile(ImageUploadFileRequest.of(FileType.IMAGE), image);
			return ruleImageRepository.save(
				RuleImage.newInstance(rule, response.getOriginalFileName(), response.getUploadFileName()));
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

	// TODO Deprecated
	public void updateRulesDeprecated(UpdateRuleRequestDto request, Long userId) {
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

	public void updateRule(UpdateRuleInfoRequestDto request, Long ruleId, Long userId, List<MultipartFile> images) {
		User user = UserServiceUtils.findUserById(userRepository, userId);
		Room room = RoomServiceUtils.findParticipatingRoom(user);
		Rule rule = RuleServiceUtils.findRuleByIdAndRoom(ruleRepository, ruleId, room);

		boolean isRuleNameDuplicate = room.getRules()
			.stream()
			.filter(roomRule -> !roomRule.getId().equals(ruleId))
			.anyMatch(roomRule -> {
				return roomRule.getName().equals(request.getName());
			});
		if (isRuleNameDuplicate) {
			throw new ConflictException(
				String.format("방 (%s) 에 이미 존재하는 ruleName (%s) 입니다.", room.getId(), request.getName()),
				CONFLICT_RULE_EXCEPTION);
		}

		var maybeRequestImages = Optional.ofNullable(images).orElse(Collections.emptyList());
		var maybeRuleImages = Optional.ofNullable(rule.getImages()).orElse(Collections.emptyList());

		RuleServiceUtils.validateRuleImageFileCounts(room, maybeRequestImages);
		rule.updateRule(request.getName(), 0, request.getDescription());

		// 이미지 s3에서 삭제
		maybeRuleImages.forEach(ruleImage -> s3Provider.deleteFile(ruleImage.getImageS3Url()));
		rule.getImages().clear();

		// 이미지 s3에 추가
		List<RuleImage> s3ImageUrls = maybeRequestImages.stream().map(image -> {
			UploadResponseDto response = s3Provider.uploadFile(ImageUploadFileRequest.of(FileType.IMAGE), image);
			return ruleImageRepository.save(
				RuleImage.newInstance(rule, response.getOriginalFileName(), response.getUploadFileName()));
		}).collect(Collectors.toList());
		rule.addAllRuleImage(s3ImageUrls);
	}

	// TODO Deprecated
	public void deleteRules(DeleteRuleRequestDto request, Long userId) {
		User user = UserServiceUtils.findUserById(userRepository, userId);
		Room room = RoomServiceUtils.findParticipatingRoom(user);
		request.getRulesIdList().forEach(ruleId -> {
			Rule rule = RuleServiceUtils.findRuleByIdAndRoom(ruleRepository, ruleId, room);
			room.deleteRule(rule);
			ruleRepository.delete(rule);
		});
	}

	public void deleteRule(Long ruleId, Long userId) {
		User user = UserServiceUtils.findUserById(userRepository, userId);
		Room room = RoomServiceUtils.findParticipatingRoom(user);
		Rule rule = RuleServiceUtils.findRuleByIdAndRoom(ruleRepository, ruleId, room);

		var maybeRuleImages = Optional.ofNullable(rule.getImages()).orElse(Collections.emptyList());

		// 이미지 s3에서 삭제
		maybeRuleImages.forEach(ruleImage -> s3Provider.deleteFile(ruleImage.getImageS3Url()));
		rule.getImages().clear();

		room.deleteRule(rule);
	}

	public void updateRepresentRule(UpdateRuleRepresentRequestDto request, Long userId) {
		User user = UserServiceUtils.findUserById(userRepository, userId);
		Room room = RoomServiceUtils.findParticipatingRoom(user);

		var validateIsPresentCount =
			request.getRules().stream().filter(represent -> Boolean.TRUE.equals(represent.getIsPresent())).count() > 3;
		if (validateIsPresentCount) {
			throw new ForbiddenException(String.format("방 (%s) 의 대표 rule 는 3 개를 초과할 수 없습니다.", room.getId()),
				FORBIDDEN_REPRESENT_RULE_COUNT_EXCEPTION);
		}

		request.getRules().forEach(represent -> {
			Rule rule = RuleServiceUtils.findRuleByIdAndRoom(ruleRepository, represent.getId(), room);
			rule.updateRuleRepresent(represent.getIsPresent());
		});
	}
}
