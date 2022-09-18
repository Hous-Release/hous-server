package hous.server.service.todo;

import hous.server.common.util.DateUtils;
import hous.server.domain.badge.Acquire;
import hous.server.domain.badge.BadgeInfo;
import hous.server.domain.badge.repository.AcquireRepository;
import hous.server.domain.badge.repository.BadgeRepository;
import hous.server.domain.common.RedisKey;
import hous.server.domain.room.Participate;
import hous.server.domain.room.Room;
import hous.server.domain.todo.Todo;
import hous.server.domain.todo.repository.DoneRepository;
import hous.server.domain.user.Onboarding;
import hous.server.domain.user.User;
import hous.server.domain.user.repository.UserRepository;
import hous.server.service.badge.BadgeServiceUtils;
import hous.server.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class TodoScheduledService {

    private final RedisTemplate<String, Object> redisTemplate;

    private final UserRepository userRepository;
    private final DoneRepository doneRepository;
    private final BadgeRepository badgeRepository;
    private final AcquireRepository acquireRepository;

    private final NotificationService notificationService;

    /**
     * 매일 0시 0분 0초마다 실행
     */
    @Scheduled(cron = "0  0  0  *  *  *")
    public void scheduledDoneMyToDos() {
        List<User> users = userRepository.findAllUsers();
        users.forEach(user -> {
            Onboarding onboarding = user.getOnboarding();
            List<Participate> participates = onboarding.getParticipates();
            if (participates.size() != 0) {
                LocalDate yesterday = DateUtils.yesterdayLocalDate();
                Room room = participates.get(0).getRoom();
                if (!BadgeServiceUtils.hasBadge(badgeRepository, acquireRepository, BadgeInfo.TODO_MASTER, onboarding)) {
                    List<Todo> todos = room.getTodos();
                    List<Todo> allDayMyTodos = TodoServiceUtils.filterAllDaysUserTodos(todos, onboarding);
                    List<Todo> yesterdayMyTodos = TodoServiceUtils.filterDayMyTodos(yesterday, onboarding, todos);
                    int yesterdayDoneMyTodosCnt = (int) yesterdayMyTodos.stream()
                            .filter(todo -> doneRepository.existsDayDoneByOnboardingAndTodo(yesterday, onboarding, todo))
                            .count();
                    if (allDayMyTodos.size() > 0 && yesterdayMyTodos.size() == yesterdayDoneMyTodosCnt) {
                        String todoCompleteCountString = (String) redisTemplate.opsForValue().get(RedisKey.TODO_COMPLETE_COUNT + user.getId());
                        if (todoCompleteCountString == null) {
                            redisTemplate.opsForValue().set(RedisKey.TODO_COMPLETE_COUNT + user.getId(), Integer.toString(1));
                        } else {
                            redisTemplate.opsForValue().set(RedisKey.TODO_COMPLETE_COUNT + user.getId(), Integer.toString(Integer.parseInt(todoCompleteCountString) + 1));
                            if (Integer.parseInt(todoCompleteCountString) == 6 && !BadgeServiceUtils.hasBadge(badgeRepository, acquireRepository, BadgeInfo.GOOD_JOB, onboarding)) {
                                Acquire acquire = acquireRepository.save(Acquire.newInstance(onboarding, badgeRepository.findBadgeByBadgeInfo(BadgeInfo.GOOD_JOB)));
                                onboarding.addAcquire(acquire);
                                notificationService.sendNewBadgeNotification(user, BadgeInfo.GOOD_JOB);
                            }
                            if (Integer.parseInt(todoCompleteCountString) == 13 && !BadgeServiceUtils.hasBadge(badgeRepository, acquireRepository, BadgeInfo.SINCERITY_KING_HOMIE, onboarding)) {
                                Acquire acquire = acquireRepository.save(Acquire.newInstance(onboarding, badgeRepository.findBadgeByBadgeInfo(BadgeInfo.SINCERITY_KING_HOMIE)));
                                onboarding.addAcquire(acquire);
                                notificationService.sendNewBadgeNotification(user, BadgeInfo.SINCERITY_KING_HOMIE);
                            }
                            if (Integer.parseInt(todoCompleteCountString) == 20 && !BadgeServiceUtils.hasBadge(badgeRepository, acquireRepository, BadgeInfo.TODO_MASTER, onboarding)) {
                                Acquire acquire = acquireRepository.save(Acquire.newInstance(onboarding, badgeRepository.findBadgeByBadgeInfo(BadgeInfo.TODO_MASTER)));
                                onboarding.addAcquire(acquire);
                                notificationService.sendNewBadgeNotification(user, BadgeInfo.TODO_MASTER);
                                redisTemplate.delete(RedisKey.TODO_COMPLETE_COUNT + user.getId());
                            }
                        }
                    } else {
                        redisTemplate.delete(RedisKey.TODO_COMPLETE_COUNT + user.getId());
                    }
                }
            }
        });
    }
}
