package hous.server.service.notification;

import hous.server.domain.badge.BadgeInfo;
import hous.server.domain.notification.Notification;
import hous.server.domain.notification.NotificationMessage;
import hous.server.domain.notification.NotificationType;
import hous.server.domain.notification.PushMessage;
import hous.server.domain.notification.mysql.NotificationRepository;
import hous.server.domain.rule.Rule;
import hous.server.domain.todo.Todo;
import hous.server.domain.user.Onboarding;
import hous.server.domain.user.PushStatus;
import hous.server.domain.user.TodoPushStatus;
import hous.server.domain.user.User;
import hous.server.service.firebase.FirebaseCloudMessageService;
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
        Notification notification = notificationRepository.save(Notification.newInstance(to.getOnboarding(), NotificationType.TODO, newTodoNotification(todo, isTake), false));
        to.getOnboarding().addNotification(notification);
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
            Notification notification = notificationRepository.save(Notification.newInstance(to.getOnboarding(), NotificationType.TODO, remindTodoNotification(todo), false));
            to.getOnboarding().addNotification(notification);
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
            Notification notification = notificationRepository.save(Notification.newInstance(to.getOnboarding(), NotificationType.RULE, newRuleNotification(rule), false));
            to.getOnboarding().addNotification(notification);
        });
        if (to.getSetting().isPushNotification() && to.getSetting().getRulesPushStatus() == PushStatus.ON) {
            firebaseCloudMessageService.sendMessageTo(to, PushMessage.NEW_RULE.getTitle(), PushMessage.NEW_RULE.getBody());
        }
    }

    public void sendNewBadgeNotification(User to, BadgeInfo badgeInfo) {
        Notification notification = notificationRepository.save(Notification.newInstance(to.getOnboarding(), NotificationType.BADGE, newBadgeNotification(badgeInfo), false));
        to.getOnboarding().addNotification(notification);
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
