package hous.api.service.migration;

import static org.assertj.core.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import hous.api.service.delete.MigrationService;
import hous.api.service.user.UserService;
import hous.api.service.user.dto.request.CreateUserRequestDto;
import hous.core.domain.badge.BadgeCounter;
import hous.core.domain.badge.BadgeCounterType;
import hous.core.domain.badge.mongo.BadgeCounterRepository;
import hous.core.domain.delete.mysql.RdbNotificationRepository;
import hous.core.domain.notification.mongo.NotificationRepository;
import hous.core.domain.user.UserSocialType;

// TODO migration - 삭제 예정

@SpringBootTest
@ActiveProfiles(value = "local")
@Transactional
public class MigrationDataTest {

	public static final String PERSONALITY_TEST_COUNT = "PTC:";
	public static final String CREATE_RULE_COUNT = "CRC:";
	public static final String TODO_COMPLETE_COUNT = "TCC:";

	@Autowired
	private UserService userService;

	@Autowired
	private MigrationService migrationService;

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	@Autowired
	private BadgeCounterRepository badgeCounterRepository;

	@Autowired
	private NotificationRepository notificationRepository;
	@Autowired
	private RdbNotificationRepository rdbNotificationRepository;

	@BeforeEach
	public void init() {
		badgeCounterRepository.deleteAll();
	}

	@Test
	@DisplayName("redis에 저장된 데이터를 mongodb로 전환 확인")
	public void transfer_redis_to_mongo_db() {
		// given
		CreateUserRequestDto createUserRequestDto1 = CreateUserRequestDto.of(
			"socialId1", UserSocialType.KAKAO, "fcmToken1", "nickname1", "2022-01-01", true);
		Long userId1 = userService.registerUser(createUserRequestDto1);

		CreateUserRequestDto createUserRequestDto2 = CreateUserRequestDto.of(
			"socialId2", UserSocialType.KAKAO, "fcmToken2", "nickname2", "2022-01-01", true);
		Long userId2 = userService.registerUser(createUserRequestDto2);

		redisTemplate.opsForValue().set(PERSONALITY_TEST_COUNT + userId1, Integer.toString(3));
		redisTemplate.opsForValue().set(PERSONALITY_TEST_COUNT + userId2, Integer.toString(2));
		redisTemplate.opsForValue().set(CREATE_RULE_COUNT + userId1, Integer.toString(2));
		redisTemplate.opsForValue().set(CREATE_RULE_COUNT + userId2, Integer.toString(1));
		redisTemplate.opsForValue().set(TODO_COMPLETE_COUNT + userId1, Integer.toString(4));
		redisTemplate.opsForValue().set(TODO_COMPLETE_COUNT + userId2, Integer.toString(2));

		// when
		migrationService.transferRedisToMongoDb();
		Set<String> redisKeys = redisTemplate.keys("*");

		// then
		BadgeCounter testScoreCounterByUser1 = badgeCounterRepository.findByUserIdAndCountType(userId1,
			BadgeCounterType.TEST_SCORE_COMPLETE);
		BadgeCounter ruleCounterByUser1 = badgeCounterRepository.findByUserIdAndCountType(userId1,
			BadgeCounterType.RULE_CREATE);
		BadgeCounter todoCounterByUser1 = badgeCounterRepository.findByUserIdAndCountType(userId1,
			BadgeCounterType.TODO_COMPLETE);

		assertThat(testScoreCounterByUser1).isNotNull();
		assertThat(testScoreCounterByUser1.getCount()).isEqualTo(3);
		assertThat(ruleCounterByUser1).isNotNull();
		assertThat(ruleCounterByUser1.getCount()).isEqualTo(2);
		assertThat(todoCounterByUser1).isNotNull();
		assertThat(todoCounterByUser1.getCount()).isEqualTo(4);

		BadgeCounter testScoreCounterByUser2 = badgeCounterRepository.findByUserIdAndCountType(userId2,
			BadgeCounterType.TEST_SCORE_COMPLETE);
		BadgeCounter ruleCounterByUser2 = badgeCounterRepository.findByUserIdAndCountType(userId2,
			BadgeCounterType.RULE_CREATE);
		BadgeCounter todoCounterByUser2 = badgeCounterRepository.findByUserIdAndCountType(userId2,
			BadgeCounterType.TODO_COMPLETE);

		assertThat(testScoreCounterByUser2).isNotNull();
		assertThat(testScoreCounterByUser2.getCount()).isEqualTo(2);
		assertThat(ruleCounterByUser2).isNotNull();
		assertThat(ruleCounterByUser2.getCount()).isEqualTo(1);
		assertThat(todoCounterByUser2).isNotNull();
		assertThat(todoCounterByUser2.getCount()).isEqualTo(2);

		assertThat(redisKeys).isEmpty();
	}
}
