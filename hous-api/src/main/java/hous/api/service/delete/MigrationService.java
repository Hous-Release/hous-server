package hous.api.service.delete;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hous.common.util.DateUtils;
import hous.core.domain.badge.BadgeCounter;
import hous.core.domain.badge.BadgeCounterType;
import hous.core.domain.badge.mongo.BadgeCounterRepository;
import hous.core.domain.delete.RdbNotification;
import hous.core.domain.delete.mysql.RdbNotificationRepository;
import hous.core.domain.notification.Notification;
import hous.core.domain.notification.mongo.NotificationRepository;
import lombok.RequiredArgsConstructor;

// TODO migration - 삭제 예정

@RequiredArgsConstructor
@Service
@Transactional
public class MigrationService {

	public static final String PERSONALITY_TEST_COUNT = "PTC:";
	public static final String CREATE_RULE_COUNT = "CRC:";
	public static final String TODO_COMPLETE_COUNT = "TCC:";

	private final RedisTemplate<String, Object> redisTemplate;

	private final BadgeCounterRepository badgeCounterRepository;
	private final RdbNotificationRepository rdbNotificationRepository;
	private final NotificationRepository notificationRepository;

	public int transferRedisToMongoDb() {
		// 전체 데이터를 가져와
		Set<String> redisKeys = redisTemplate.keys("*");
		for (String key : redisKeys) {
			Long userId = Long.parseLong(key.substring(key.indexOf(":") + 1));
			// key 값에 따라 몽고디비에 저장해
			if (key.startsWith(PERSONALITY_TEST_COUNT)) {
				BadgeCounter personalityTestCounter = getBadgeCounterByCounterType(userId,
					BadgeCounterType.TEST_SCORE_COMPLETE);
				migrationBadgeCount(key, personalityTestCounter);
			}

			if (key.startsWith(CREATE_RULE_COUNT)) {
				BadgeCounter ruleCounter = getBadgeCounterByCounterType(userId, BadgeCounterType.RULE_CREATE);
				migrationBadgeCount(key, ruleCounter);
			}

			if (key.startsWith(TODO_COMPLETE_COUNT)) {
				BadgeCounter todoCounter = getBadgeCounterByCounterType(userId, BadgeCounterType.TODO_COMPLETE);
				migrationBadgeCount(key, todoCounter);
			}
		}
		return redisKeys.size();
	}

	private BadgeCounter getBadgeCounterByCounterType(Long userId, BadgeCounterType badgeCounterType) {
		BadgeCounter badgeCounter = badgeCounterRepository.findByUserIdAndCountType(userId, badgeCounterType);
		if (badgeCounter == null) {
			badgeCounter = badgeCounterRepository.save(BadgeCounter.newInstance(userId, badgeCounterType, 0));
		}
		return badgeCounter;
	}

	private void migrationBadgeCount(String redisKey, BadgeCounter badgeCounter) {
		String count = (String)redisTemplate.opsForValue().get(redisKey);
		if (count != null) {
			badgeCounter.updateCount(Integer.parseInt(count));
			badgeCounterRepository.save(badgeCounter);
			redisTemplate.delete(redisKey);
		}
	}

	public List<Integer> transferNotificationToMongoDb() {
		List<RdbNotification> rdbNotifications = rdbNotificationRepository.findAllByOrderByIdAsc();
		LocalDateTime twentyEightDaysAgo = DateUtils.todayLocalDateTime().minusDays(28);
		AtomicInteger successCount = new AtomicInteger();
		rdbNotifications.stream()
			// 28일이 이미 지난 데이터는 건너뛰셈
			.filter(rdbNotification -> rdbNotification.getCreatedAt().isAfter(twentyEightDaysAgo))
			.forEach(rdbNotification -> {
				// 생성시간 그대로 저장해야함 수정시간도 저장
				// 사라질 시간은 오늘 기준 30일 전
				Notification notification = Notification.migrationInstance(rdbNotification.getId(),
					rdbNotification.getOnboarding().getId(),
					rdbNotification.getType(), rdbNotification.getContent(), rdbNotification.isRead(),
					rdbNotification.getCreatedAt());
				notification.setCreatedAt(rdbNotification.getCreatedAt());
				notification.setUpdatedAt(rdbNotification.getUpdatedAt());
				notificationRepository.save(notification);
				successCount.getAndIncrement();
			});
		return List.of(rdbNotifications.size(), successCount.get());
	}
}
