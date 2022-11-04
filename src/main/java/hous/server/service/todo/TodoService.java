package hous.server.service.todo;

import hous.server.common.util.DateUtils;
import hous.server.domain.badge.BadgeInfo;
import hous.server.domain.room.Room;
import hous.server.domain.todo.Done;
import hous.server.domain.todo.Redo;
import hous.server.domain.todo.Take;
import hous.server.domain.todo.Todo;
import hous.server.domain.todo.repository.DoneRepository;
import hous.server.domain.todo.repository.RedoRepository;
import hous.server.domain.todo.repository.TakeRepository;
import hous.server.domain.todo.repository.TodoRepository;
import hous.server.domain.user.Onboarding;
import hous.server.domain.user.User;
import hous.server.domain.user.repository.OnboardingRepository;
import hous.server.domain.user.repository.UserRepository;
import hous.server.service.badge.BadgeService;
import hous.server.service.notification.NotificationService;
import hous.server.service.room.RoomServiceUtils;
import hous.server.service.todo.dto.request.CheckTodoRequestDto;
import hous.server.service.todo.dto.request.TodoInfoRequestDto;
import hous.server.service.user.UserServiceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class TodoService {

    private final UserRepository userRepository;
    private final OnboardingRepository onboardingRepository;
    private final TodoRepository todoRepository;
    private final TakeRepository takeRepository;
    private final RedoRepository redoRepository;
    private final DoneRepository doneRepository;

    private final BadgeService badgeService;
    private final NotificationService notificationService;

    public void createTodo(TodoInfoRequestDto request, Long userId) {
        User user = UserServiceUtils.findUserById(userRepository, userId);
        Room room = RoomServiceUtils.findParticipatingRoom(user);
        TodoServiceUtils.validateTodoCounts(room);
        Todo todo = todoRepository.save(Todo.newInstance(room, request.getName(), request.isPushNotification()));
        request.getTodoUsers().forEach(todoUser -> {
            Onboarding onboarding = onboardingRepository.findOnboardingById(todoUser.getOnboardingId());
            Take take = takeRepository.save(Take.newInstance(todo, onboarding));
            todoUser.getDayOfWeeks().forEach(dayOfWeek -> {
                Redo redo = redoRepository.save(Redo.newInstance(take, dayOfWeek));
                take.addRedo(redo);
            });
            todo.addTake(take);
        });
        room.addTodo(todo);
        badgeService.acquireBadge(user, BadgeInfo.TODO_ONE_STEP);
        List<User> usersExceptMe = RoomServiceUtils.findParticipatingUsersExceptMe(room, user);
        usersExceptMe.forEach(userExceptMe -> {
            List<Onboarding> onboardings = todo.getTakes().stream()
                    .map(Take::getOnboarding)
                    .collect(Collectors.toList());
            notificationService.sendNewTodoNotification(userExceptMe, todo, onboardings.contains(userExceptMe.getOnboarding()));
        });
    }

    public void updateTodo(Long todoId, TodoInfoRequestDto request) {
        Todo todo = TodoServiceUtils.findTodoById(todoRepository, todoId);
        Room room = todo.getRoom();
        todo.getTakes().forEach(take -> {
            redoRepository.deleteAll(take.getRedos());
            takeRepository.delete(take);
        });
        List<Take> takes = new ArrayList<>();
        request.getTodoUsers().forEach(todoUser -> {
            Onboarding onboarding = onboardingRepository.findOnboardingById(todoUser.getOnboardingId());
            Take take = takeRepository.save(Take.newInstance(todo, onboarding));
            todoUser.getDayOfWeeks().forEach(dayOfWeek -> {
                Redo redo = redoRepository.save(Redo.newInstance(take, dayOfWeek));
                take.addRedo(redo);
            });
            takes.add(take);
        });
        todo.updateTodo(request.getName(), request.isPushNotification(), takes);
        room.updateTodo(todo);
    }

    public void checkTodo(Long todoId, CheckTodoRequestDto request, Long userId) {
        User user = UserServiceUtils.findUserById(userRepository, userId);
        Todo todo = TodoServiceUtils.findTodoById(todoRepository, todoId);
        Onboarding onboarding = user.getOnboarding();
        TodoServiceUtils.validateTodoStatus(doneRepository, request.isStatus(), onboarding, todo);
        if (request.isStatus()) {
            Done done = doneRepository.save(Done.newInstance(onboarding, todo));
            todo.addDone(done);
        } else {
            Done done = doneRepository.findTodayDoneByOnboardingAndTodo(DateUtils.todayLocalDate(), onboarding, todo);
            if (done != null) {
                todo.deleteDone(done);
                doneRepository.delete(done);
            }
        }
    }

    public void deleteTodo(Long todoId) {
        Todo todo = TodoServiceUtils.findTodoById(todoRepository, todoId);
        Room room = todo.getRoom();
        todo.getTakes().forEach(take -> {
            redoRepository.deleteAll(take.getRedos());
            takeRepository.delete(take);
        });
        doneRepository.deleteAll(todo.getDones());
        room.deleteTodo(todo);
        todoRepository.delete(todo);
    }
}
