package hous.api.service.todo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hous.api.service.badge.BadgeService;
import hous.api.service.notification.NotificationService;
import hous.api.service.room.RoomServiceUtils;
import hous.api.service.todo.dto.request.CheckTodoRequestDto;
import hous.api.service.todo.dto.request.TodoInfoRequestDto;
import hous.api.service.user.UserServiceUtils;
import hous.common.util.DateUtils;
import hous.core.domain.badge.BadgeInfo;
import hous.core.domain.room.Room;
import hous.core.domain.todo.Done;
import hous.core.domain.todo.Redo;
import hous.core.domain.todo.Take;
import hous.core.domain.todo.Todo;
import hous.core.domain.todo.mysql.DoneRepository;
import hous.core.domain.todo.mysql.RedoRepository;
import hous.core.domain.todo.mysql.TakeRepository;
import hous.core.domain.todo.mysql.TodoRepository;
import hous.core.domain.user.Onboarding;
import hous.core.domain.user.User;
import hous.core.domain.user.mysql.OnboardingRepository;
import hous.core.domain.user.mysql.UserRepository;
import lombok.RequiredArgsConstructor;

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
		TodoServiceUtils.existsTodoByRoomTodos(room, request.getName(), null);
		Todo todo = todoRepository.save(Todo.newInstance(room, request.getName(), request.isPushNotification()));
		request.getTodoUsers().forEach(todoUser -> {
			Onboarding onboarding = UserServiceUtils.findOnboardingById(onboardingRepository,
				todoUser.getOnboardingId());
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
			notificationService.sendNewTodoNotification(userExceptMe, todo,
				onboardings.contains(userExceptMe.getOnboarding()));
		});
	}

	public void updateTodo(Long todoId, TodoInfoRequestDto request, Long userId) {
		UserServiceUtils.findUserById(userRepository, userId);
		Todo todo = TodoServiceUtils.findTodoById(todoRepository, todoId);
		Room room = todo.getRoom();
		TodoServiceUtils.existsTodoByRoomTodos(room, request.getName(), todo);
		todo.getTakes().forEach(take -> {
			redoRepository.deleteAll(take.getRedos());
			takeRepository.delete(take);
		});
		List<Take> takes = new ArrayList<>();
		request.getTodoUsers().forEach(todoUser -> {
			Onboarding onboarding = UserServiceUtils.findOnboardingById(onboardingRepository,
				todoUser.getOnboardingId());
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

	public void deleteTodo(Long todoId, Long userId) {
		UserServiceUtils.findUserById(userRepository, userId);
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
