package hous.api.service.notification;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hous.api.config.sqs.producer.SqsProducer;
import hous.common.dto.sqs.FirebaseDto;
import hous.core.domain.badge.BadgeInfo;
import hous.core.domain.notification.Notification;
import hous.core.domain.notification.NotificationMessage;
import hous.core.domain.notification.NotificationType;
import hous.core.domain.notification.PushMessage;
import hous.core.domain.notification.mongo.NotificationRepository;
import hous.core.domain.rule.Rule;
import hous.core.domain.todo.Todo;
import hous.core.domain.user.PushStatus;
import hous.core.domain.user.TodoPushStatus;
import hous.core.domain.user.User;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional
public class NotificationService {

	private final NotificationRepository notificationRepository;
	private final SqsProducer sqsProducer;

	public void sendNewTodoNotification(User to, Todo todo, boolean isTake) {
		notificationRepository.save(
			Notification.newInstance(to.getOnboarding().getId(), NotificationType.TODO,
				generateContent(todo.getName(), getTodoMessage(isTake)),
				false));

		if (todo.isPushNotification() && to.getSetting().isPushNotification()) {
			TodoPushStatus todoPushStatus = to.getSetting().getNewTodoPushStatus();
			String title = "";
			String body = "";
			if (todoPushStatus == TodoPushStatus.ON_ALL) {
				title = PushMessage.NEW_TODO.getTitle();
				body = PushMessage.NEW_TODO.getBody();
			}
			if (todoPushStatus == TodoPushStatus.ON_MY && isTake) {
				title = PushMessage.NEW_TODO_TAKE.getTitle();
				body = PushMessage.NEW_TODO_TAKE.getBody();
			}
			sqsProducer.produce(FirebaseDto.of(to.getFcmToken(), title, body));
		}
	}

	public void sendTodayTodoNotification(User to, boolean isTake) {
		if (to.getSetting().isPushNotification()) {
			TodoPushStatus todoPushStatus = to.getSetting().getTodayTodoPushStatus();
			String title = "";
			String body = "";
			if (todoPushStatus == TodoPushStatus.ON_ALL) {
				title = PushMessage.TODAY_TODO_START.getTitle();
				body = PushMessage.TODAY_TODO_START.getBody();
			}
			if (todoPushStatus == TodoPushStatus.ON_MY && isTake) {
				title = PushMessage.TODAY_TODO_TAKE_START.getTitle();
				body = PushMessage.TODAY_TODO_TAKE_START.getBody();
			}
			sqsProducer.produce(FirebaseDto.of(to.getFcmToken(), title, body));
		}
	}

	public void sendRemindTodoNotification(User to, List<Todo> todos, boolean isTake) {
		todos.forEach(todo ->
			notificationRepository.save(
				Notification.newInstance(to.getOnboarding().getId(), NotificationType.TODO,
					generateContent(todo.getName(), NotificationMessage.TODO_REMIND.getValue()),
					false))
		);

		if (to.getSetting().isPushNotification()) {
			TodoPushStatus todoPushStatus = to.getSetting().getRemindTodoPushStatus();
			String title = "";
			String body = "";
			if (todoPushStatus == TodoPushStatus.ON_ALL) {
				title = PushMessage.TODO_REMIND.getTitle();
				body = PushMessage.TODO_REMIND.getBody();
			}
			if (todoPushStatus == TodoPushStatus.ON_MY
				&& isTake) {
				title = PushMessage.TODO_TAKE_REMIND.getTitle();
				body = PushMessage.TODO_TAKE_REMIND.getBody();
			}
			sqsProducer.produce(FirebaseDto.of(to.getFcmToken(), title, body));
		}
	}

	// TODO Deprecated
	public void sendNewRuleNotification(User to, List<Rule> rules) {
		rules.forEach(rule ->
			notificationRepository.save(
				Notification.newInstance(to.getOnboarding().getId(), NotificationType.RULE,
					generateContent(rule.getName(), NotificationMessage.NEW_RULE.getValue()),
					false))
		);

		if (to.getSetting().isPushNotification() && to.getSetting().getRulesPushStatus() == PushStatus.ON) {
			sqsProducer.produce(
				FirebaseDto.of(to.getFcmToken(), PushMessage.NEW_RULE.getTitle(), PushMessage.NEW_RULE.getBody()));
		}
	}

	public void sendNewRuleNotification(User to, Rule rule) {
		notificationRepository.save(
			Notification.newInstance(to.getOnboarding().getId(), NotificationType.RULE,
				generateContent(rule.getName(), NotificationMessage.NEW_RULE.getValue()),
				false));

		if (to.getSetting().isPushNotification() && to.getSetting().getRulesPushStatus() == PushStatus.ON) {
			sqsProducer.produce(
				FirebaseDto.of(to.getFcmToken(), PushMessage.NEW_RULE.getTitle(), PushMessage.NEW_RULE.getBody()));
		}
	}

	public void sendNewBadgeNotification(User to, BadgeInfo badgeInfo) {
		notificationRepository.save(
			Notification.newInstance(to.getOnboarding().getId(), NotificationType.BADGE,
				generateContent(badgeInfo.getValue(), NotificationMessage.NEW_BADGE.getValue()),
				false)
		);

		if (to.getSetting().isPushNotification() && to.getSetting().getBadgePushStatus() == PushStatus.ON) {
			sqsProducer.produce(FirebaseDto.of(to.getFcmToken(),
				generateContent(badgeInfo.getValue(), PushMessage.NEW_BADGE.getTitle()),
				generateDetailContent(to.getOnboarding().getNickname(), PushMessage.NEW_BADGE.getBody())));
		}
	}

	private String generateContent(String name, String message) {
		return String.format("'%s' %s", name, message);
	}

	private String generateDetailContent(String name, String message) {
		return String.format("%s%s", name, message);
	}

	private String getTodoMessage(boolean isTake) {
		if (isTake) {
			return NotificationMessage.NEW_TODO_TAKE.getValue();
		}
		return NotificationMessage.NEW_TODO.getValue();
	}
}
