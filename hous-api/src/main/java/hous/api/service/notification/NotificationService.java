package hous.api.service.notification;

import hous.api.service.firebase.FirebaseCloudMessageService;
import hous.core.domain.badge.BadgeInfo;
import hous.core.domain.notification.Notification;
import hous.core.domain.notification.NotificationMessage;
import hous.core.domain.notification.NotificationType;
import hous.core.domain.notification.PushMessage;
import hous.core.domain.notification.mongo.NotificationRepository;
import hous.core.domain.rule.Rule;
import hous.core.domain.todo.Todo;
import hous.core.domain.user.Onboarding;
import hous.core.domain.user.PushStatus;
import hous.core.domain.user.TodoPushStatus;
import hous.core.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final FirebaseCloudMessageService firebaseCloudMessageService;

    public void sendNewTodoNotification(User to, Todo todo, boolean isTake) {
        notificationRepository.save(Notification.newInstance(to.getOnboarding().getId(), NotificationType.TODO, newTodoNotification(todo, isTake), false));
        if (todo.isPushNotification() && to.getSetting().isPushNotification() && to.getSetting().getNewTodoPushStatus() == TodoPushStatus.ON_ALL) {
            firebaseCloudMessageService.sendMessageTo(to, PushMessage.NEW_TODO.getTitle(), PushMessage.NEW_TODO.getBody());
        }
        if (todo.isPushNotification() && to.getSetting().isPushNotification() && to.getSetting().getNewTodoPushStatus() == TodoPushStatus.ON_MY && isTake) {
            firebaseCloudMessageService.sendMessageTo(to, PushMessage.NEW_TODO_TAKE.getTitle(), PushMessage.NEW_TODO_TAKE.getBody());
        }
    }

    public void sendTodayTodoNotification(User to, boolean isTake) {
        if (to.getSetting().isPushNotification() && to.getSetting().getTodayTodoPushStatus() == TodoPushStatus.ON_ALL) {
            firebaseCloudMessageService.sendMessageTo(to, PushMessage.TODAY_TODO_START.getTitle(), PushMessage.TODAY_TODO_START.getBody());
        }
        if (to.getSetting().isPushNotification() && to.getSetting().getTodayTodoPushStatus() == TodoPushStatus.ON_MY && isTake) {
            firebaseCloudMessageService.sendMessageTo(to, PushMessage.TODAY_TODO_TAKE_START.getTitle(), PushMessage.TODAY_TODO_TAKE_START.getBody());
        }
    }

    public void sendRemindTodoNotification(User to, List<Todo> todos, boolean isTake) {
        todos.forEach(todo -> {
            notificationRepository.save(Notification.newInstance(to.getOnboarding().getId(), NotificationType.TODO, remindTodoNotification(todo), false));
        });
        if (to.getSetting().isPushNotification() && to.getSetting().getRemindTodoPushStatus() == TodoPushStatus.ON_ALL) {
            firebaseCloudMessageService.sendMessageTo(to, PushMessage.TODO_REMIND.getTitle(), PushMessage.TODO_REMIND.getBody());
        }
        if (to.getSetting().isPushNotification() && to.getSetting().getRemindTodoPushStatus() == TodoPushStatus.ON_MY && isTake) {
            firebaseCloudMessageService.sendMessageTo(to, PushMessage.TODO_TAKE_REMIND.getTitle(), PushMessage.TODO_TAKE_REMIND.getBody());
        }
    }

    public void sendNewRuleNotification(User to, List<Rule> rules) {
        rules.stream().forEach(rule -> {
            notificationRepository.save(Notification.newInstance(to.getOnboarding().getId(), NotificationType.RULE, newRuleNotification(rule), false));
        });
        if (to.getSetting().isPushNotification() && to.getSetting().getRulesPushStatus() == PushStatus.ON) {
            firebaseCloudMessageService.sendMessageTo(to, PushMessage.NEW_RULE.getTitle(), PushMessage.NEW_RULE.getBody());
        }
    }

    public void sendNewBadgeNotification(User to, BadgeInfo badgeInfo) {
        notificationRepository.save(Notification.newInstance(to.getOnboarding().getId(), NotificationType.BADGE, newBadgeNotification(badgeInfo), false));
        if (to.getSetting().isPushNotification() && to.getSetting().getBadgePushStatus() == PushStatus.ON) {
            firebaseCloudMessageService.sendMessageTo(to, newBadgePushTitle(badgeInfo), newBadgePushBody(to.getOnboarding()));
        }
    }

    private String newTodoNotification(Todo todo, boolean isTake) {
        if (isTake) return String.format("'%s' %s", todo.getName(), NotificationMessage.NEW_TODO_TAKE.getValue());
        else return String.format("'%s' %s", todo.getName(), NotificationMessage.NEW_TODO.getValue());
    }

    private String remindTodoNotification(Todo todo) {
        return String.format("'%s' %s", todo.getName(), NotificationMessage.TODO_REMIND.getValue());
    }

    private String newRuleNotification(Rule rule) {
        return String.format("'%s' %s", rule.getName(), NotificationMessage.NEW_RULE.getValue());
    }

    private String newBadgePushTitle(BadgeInfo badgeInfo) {
        return String.format("'%s' %s", badgeInfo.getValue(), PushMessage.NEW_BADGE.getTitle());
    }

    private String newBadgePushBody(Onboarding onboarding) {
        return String.format("%s%s", onboarding.getNickname(), PushMessage.NEW_BADGE.getBody());
    }

    private String newBadgeNotification(BadgeInfo badgeInfo) {
        return String.format("'%s' %s", badgeInfo.getValue(), NotificationMessage.NEW_BADGE.getValue());
    }
}