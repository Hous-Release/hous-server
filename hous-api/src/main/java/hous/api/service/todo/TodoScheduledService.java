package hous.api.service.todo;

import hous.api.service.badge.BadgeService;
import hous.api.service.badge.BadgeServiceUtils;
import hous.api.service.notification.NotificationService;
import hous.common.util.DateUtils;
import hous.core.domain.badge.BadgeCounter;
import hous.core.domain.badge.BadgeCounterType;
import hous.core.domain.badge.BadgeInfo;
import hous.core.domain.badge.mongo.BadgeCounterRepository;
import hous.core.domain.badge.mysql.AcquireRepository;
import hous.core.domain.badge.mysql.BadgeRepository;
import hous.core.domain.room.Participate;
import hous.core.domain.room.Room;
import hous.core.domain.todo.OurTodoStatus;
import hous.core.domain.todo.Todo;
import hous.core.domain.todo.mysql.DoneRepository;
import hous.core.domain.user.Onboarding;
import hous.core.domain.user.User;
import hous.core.domain.user.mysql.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class TodoScheduledService {

    private final RedisTemplate<String, Object> redisTemplate;

    private final UserRepository userRepository;
    private final DoneRepository doneRepository;
    private final BadgeRepository badgeRepository;
    private final AcquireRepository acquireRepository;
    private final BadgeCounterRepository badgeCounterRepository;

    private final BadgeService badgeService;
    private final NotificationService notificationService;

    /**
     * 매일 0시 0분 0초마다 실행
     */
    @Scheduled(cron = "0  0  0  *  *  *")
    public void scheduledDoneMyTodos() {
        List<User> users = userRepository.findAllUsers();
        users.forEach(user -> {
            Onboarding onboarding = user.getOnboarding();
            List<Participate> participates = onboarding.getParticipates();
            if (!participates.isEmpty()) {
                LocalDate yesterday = DateUtils.yesterdayLocalDate();
                Room room = participates.get(0).getRoom();
                if (!BadgeServiceUtils.hasBadge(badgeRepository, acquireRepository, BadgeInfo.TODO_MASTER, onboarding)) {
                    List<Todo> todos = room.getTodos();
                    List<Todo> allDayMyTodos = TodoServiceUtils.filterAllDaysUserTodos(todos, onboarding);
                    List<Todo> yesterdayMyTodos = TodoServiceUtils.filterDayMyTodos(yesterday, onboarding, todos);
                    int yesterdayDoneMyTodosCnt = (int) yesterdayMyTodos.stream()
                            .filter(todo -> doneRepository.existsDayDoneByOnboardingAndTodo(yesterday, onboarding, todo))
                            .count();
                    if (!allDayMyTodos.isEmpty() && yesterdayMyTodos.size() == yesterdayDoneMyTodosCnt) {
                        BadgeCounter todoComplete = badgeCounterRepository.findByUserIdAndCountType(user.getId(), BadgeCounterType.TODO_COMPLETE);
                        if (todoComplete == null) {
                            badgeCounterRepository.save(BadgeCounter.newInstance(user.getId(), BadgeCounterType.TODO_COMPLETE, 1));
                        } else {
                            todoComplete.updateCount(todoComplete.getCount() + 1);
                            badgeCounterRepository.save(todoComplete);
                            if (todoComplete.getCount() == 6) {
                                badgeService.acquireBadge(user, BadgeInfo.GOOD_JOB);
                            }
                            if (todoComplete.getCount() == 13) {
                                badgeService.acquireBadge(user, BadgeInfo.SINCERITY_KING_HOMIE);
                            }
                            if (todoComplete.getCount() == 20) {
                                badgeService.acquireBadge(user, BadgeInfo.TODO_MASTER);
                                badgeCounterRepository.deleteBadgeCounterByUserIdAndCountType(user.getId(), BadgeCounterType.TODO_COMPLETE);
                            }
                        }
                    } else {
                        badgeCounterRepository.deleteBadgeCounterByUserIdAndCountType(user.getId(), BadgeCounterType.TODO_COMPLETE);
                    }
                }
            }
        });
    }

    /**
     * 매일 9시 0분 0초마다 실행
     */
    @Scheduled(cron = "0  0  9  *  *  *")
    public void scheduledTodayTodos() {
        List<User> users = userRepository.findAllUsers();
        users.forEach(user -> {
            Onboarding onboarding = user.getOnboarding();
            List<Participate> participates = onboarding.getParticipates();
            if (!participates.isEmpty()) {
                LocalDate today = DateUtils.todayLocalDate();
                Room room = participates.get(0).getRoom();
                List<Todo> todayOurTodos = TodoServiceUtils.filterDayOurTodosByIsPushNotification(today, room.getTodos());
                List<Todo> todayMyTodos = TodoServiceUtils.filterDayMyTodosByIsPushNotification(today, onboarding, room.getTodos());
                if (!todayOurTodos.isEmpty()) {
                    notificationService.sendTodayTodoNotification(user, !todayMyTodos.isEmpty());
                }
            }
        });
    }

    /**
     * 매일 22시 0분 0초마다 실행
     */
    @Scheduled(cron = "0  0  22  *  *  *")
    public void scheduledRemindTodos() {
        List<User> users = userRepository.findAllUsers();
        users.forEach(user -> {
            Onboarding onboarding = user.getOnboarding();
            List<Participate> participates = onboarding.getParticipates();
            if (!participates.isEmpty()) {
                LocalDate today = DateUtils.todayLocalDate();
                Room room = participates.get(0).getRoom();
                List<Todo> todayOurTodos = TodoServiceUtils.filterDayOurTodosByIsPushNotification(today, room.getTodos());
                List<Todo> todayMyTodos = TodoServiceUtils.filterDayMyTodosByIsPushNotification(today, onboarding, room.getTodos());
                List<Todo> undoneTodayOurTodos = todayOurTodos.stream()
                        .filter(todayOurTodo -> doneRepository.findTodayOurTodoStatus(today, todayOurTodo) != OurTodoStatus.FULL_CHECK)
                        .collect(Collectors.toList());
                List<Todo> undoneTodayMyTodos = todayMyTodos.stream()
                        .filter(todayOurTodo -> !doneRepository.findTodayTodoCheckStatus(today, onboarding, todayOurTodo))
                        .collect(Collectors.toList());
                if (!undoneTodayOurTodos.isEmpty()) {
                    notificationService.sendRemindTodoNotification(user, undoneTodayOurTodos, !undoneTodayMyTodos.isEmpty());
                }
            }
        });
    }
}
