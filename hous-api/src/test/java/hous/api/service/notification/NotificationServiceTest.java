package hous.api.service.notification;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import hous.api.service.user.UserService;
import hous.api.service.user.dto.request.CreateUserRequestDto;
import hous.core.domain.badge.BadgeInfo;
import hous.core.domain.notification.Notification;
import hous.core.domain.notification.NotificationType;
import hous.core.domain.notification.mongo.NotificationRepository;
import hous.core.domain.room.Room;
import hous.core.domain.rule.Rule;
import hous.core.domain.todo.Todo;
import hous.core.domain.user.User;
import hous.core.domain.user.UserSocialType;
import hous.core.domain.user.mysql.UserRepository;

@SpringBootTest
@ActiveProfiles(value = "local")
@Transactional
public class NotificationServiceTest {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private NotificationRepository notificationRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private NotificationService notificationService;

	@BeforeEach
	public void init() {
		notificationRepository.deleteAll();
	}

	@Test
	@DisplayName("새로운 to-do 추가 알림 전송 성공")
	public void send_new_todo_notification_success() {
		// given
		CreateUserRequestDto createUserRequestDto = CreateUserRequestDto.of(
			"socialId1", UserSocialType.KAKAO, "fcmToken1", "nickname1", "2022-01-01", true);
		Long userId = userService.registerUser(createUserRequestDto);
		User user = userRepository.findUserById(userId);
		Room room = Room.newInstance(user.getOnboarding(), "room1", "code1");
		Todo todo = Todo.newInstance(room, "todo1", true);

		// when
		notificationService.sendNewTodoNotification(user, todo, true);

		// then
		List<Notification> notifications = notificationRepository.findAll();
		assertThat(notifications).hasSize(1);
		assertThat(notifications.get(0).getType()).isEqualTo(NotificationType.TODO);
	}

	@Test
	@DisplayName("미완료 to-do 알림 전송 성공")
	public void send_remind_todo_notification_success() {
		// given
		CreateUserRequestDto createUserRequestDto = CreateUserRequestDto.of(
			"socialId1", UserSocialType.KAKAO, "fcmToken1", "nickname1", "2022-01-01", true);
		Long userId = userService.registerUser(createUserRequestDto);
		User user = userRepository.findUserById(userId);
		Room room = Room.newInstance(user.getOnboarding(), "room1", "code1");
		Todo todo = Todo.newInstance(room, "todo1", true);

		// when
		notificationService.sendRemindTodoNotification(user, List.of(todo), true);

		// then
		List<Notification> notifications = notificationRepository.findAll();
		assertThat(notifications).hasSize(1);
		assertThat(notifications.get(0).getType()).isEqualTo(NotificationType.TODO);
	}

	@Test
	@DisplayName("새로운 Rules 추가 알림 전송 성공")
	public void send_new_rule_notification_success() {
		// given
		CreateUserRequestDto createUserRequestDto = CreateUserRequestDto.of(
			"socialId1", UserSocialType.KAKAO, "fcmToken1", "nickname1", "2022-01-01", true);
		Long userId = userService.registerUser(createUserRequestDto);
		User user = userRepository.findUserById(userId);
		Room room = Room.newInstance(user.getOnboarding(), "room1", "code1");
		Rule rule = Rule.newInstance(room, "rule1", 1, "");

		// when
		notificationService.sendNewRuleNotification(user, List.of(rule));

		// then
		List<Notification> notifications = notificationRepository.findAll();
		assertThat(notifications).hasSize(1);
		assertThat(notifications.get(0).getType()).isEqualTo(NotificationType.RULE);
	}

	@Test
	@DisplayName("새로운 배지 알림 전송 성공")
	public void send_new_badge_notification_success() {
		// given
		CreateUserRequestDto createUserRequestDto = CreateUserRequestDto.of(
			"socialId1", UserSocialType.KAKAO, "fcmToken1", "nickname1", "2022-01-01", true);
		Long userId = userService.registerUser(createUserRequestDto);
		User user = userRepository.findUserById(userId);

		// when
		notificationService.sendNewBadgeNotification(user, BadgeInfo.POUNDING_HOUSE);

		// then
		List<Notification> notifications = notificationRepository.findAll();
		assertThat(notifications).hasSize(1);
		assertThat(notifications.get(0).getType()).isEqualTo(NotificationType.BADGE);
	}
}
