package hous.api.service.todo;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hous.api.service.room.RoomServiceUtils;
import hous.api.service.todo.dto.response.MyTodoInfoResponse;
import hous.api.service.todo.dto.response.OurTodoInfo;
import hous.api.service.todo.dto.response.TodoAddableResponse;
import hous.api.service.todo.dto.response.TodoDetailInfo;
import hous.api.service.todo.dto.response.TodoFilterResponse;
import hous.api.service.todo.dto.response.TodoInfoResponse;
import hous.api.service.todo.dto.response.TodoMainResponse;
import hous.api.service.todo.dto.response.TodoSummaryInfoResponse;
import hous.api.service.todo.dto.response.UserPersonalityInfo;
import hous.api.service.todo.dto.response.UserPersonalityInfoResponse;
import hous.api.service.user.UserServiceUtils;
import hous.common.constant.Constraint;
import hous.common.util.DateUtils;
import hous.core.domain.common.AuditingTimeEntity;
import hous.core.domain.room.Participate;
import hous.core.domain.room.Room;
import hous.core.domain.todo.DayOfWeek;
import hous.core.domain.todo.Take;
import hous.core.domain.todo.Todo;
import hous.core.domain.todo.mysql.DoneRepository;
import hous.core.domain.todo.mysql.TodoRepository;
import hous.core.domain.user.Onboarding;
import hous.core.domain.user.User;
import hous.core.domain.user.mysql.UserRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class TodoRetrieveService {

	private final UserRepository userRepository;
	private final TodoRepository todoRepository;
	private final DoneRepository doneRepository;

	public TodoAddableResponse getTodoAddable(Long userId) {
		User user = UserServiceUtils.findUserById(userRepository, userId);
		Room room = RoomServiceUtils.findParticipatingRoom(user);
		List<Todo> todos = room.getTodos();
		return TodoAddableResponse.of(todos.size() < Constraint.TODO_COUNT_MAX);
	}

	public UserPersonalityInfoResponse getUsersInfo(Long userId) {
		User user = UserServiceUtils.findUserById(userRepository, userId);
		Room room = RoomServiceUtils.findParticipatingRoom(user);
		List<Participate> participates = room.getParticipates();
		List<Onboarding> onboardings = participates.stream()
			.map(Participate::getOnboarding)
			.sorted(Onboarding::compareTo)
			.collect(Collectors.toList());
		List<Onboarding> meFirstList = UserServiceUtils.toMeFirstList(onboardings, user.getOnboarding());
		return UserPersonalityInfoResponse.of(meFirstList);
	}

	public TodoMainResponse getTodoMain(Long userId) {
		User user = UserServiceUtils.findUserById(userRepository, userId);
		Onboarding onboarding = user.getOnboarding();
		Room room = RoomServiceUtils.findParticipatingRoom(user);
		LocalDate today = DateUtils.todayLocalDate();
		List<Todo> todos = room.getTodos();
		List<Todo> todayOurTodosList = TodoServiceUtils.filterDayOurTodos(today, todos);
		List<Todo> todayMyTodosList = TodoServiceUtils.filterDayMyTodos(today, onboarding, todos);
		List<TodoDetailInfo> todayMyTodos = todayMyTodosList.stream()
			.sorted(Comparator.comparing(AuditingTimeEntity::getCreatedAt))
			.map(todo -> TodoDetailInfo.of(todo.getId(), todo.getName(),
				doneRepository.findTodayTodoCheckStatus(today, onboarding, todo)))
			.collect(Collectors.toList());
		List<OurTodoInfo> todayOurTodos = todayOurTodosList.stream()
			.sorted(Comparator.comparing(AuditingTimeEntity::getCreatedAt))
			.map(todo -> OurTodoInfo.of(todo.getName(), doneRepository.findTodayOurTodoStatus(today, todo),
				todo.getTakes().stream()
					.map(Take::getOnboarding)
					.collect(Collectors.toSet()), onboarding))
			.collect(Collectors.toList());
		return TodoMainResponse.of(today, todayMyTodos, todayOurTodos);
	}

	public TodoInfoResponse getTodoInfo(Long todoId, Long userId) {
		User user = UserServiceUtils.findUserById(userRepository, userId);
		Room room = RoomServiceUtils.findParticipatingRoom(user);
		Todo todo = TodoServiceUtils.findTodoById(todoRepository, todoId);
		List<Participate> participates = room.getParticipates();
		List<Onboarding> onboardings = participates.stream()
			.map(Participate::getOnboarding)
			.sorted(Onboarding::compareTo)
			.collect(Collectors.toList());
		List<Onboarding> meFirstList = UserServiceUtils.toMeFirstList(onboardings, user.getOnboarding());
		List<Onboarding> todoTakes = todo.getTakes().stream()
			.map(Take::getOnboarding)
			.sorted(Onboarding::compareTo)
			.collect(Collectors.toList());
		List<Onboarding> meFirstTodoTakes = UserServiceUtils.toMeFirstList(todoTakes, user.getOnboarding());
		List<UserPersonalityInfo> userPersonalityInfos = TodoServiceUtils.toUserPersonalityInfoList(meFirstTodoTakes);
		return TodoInfoResponse.of(todo, userPersonalityInfos, meFirstList);
	}

	public TodoSummaryInfoResponse getTodoSummaryInfo(Long todoId, Long userId) {
		User user = UserServiceUtils.findUserById(userRepository, userId);
		RoomServiceUtils.findParticipatingRoom(user);
		Todo todo = TodoServiceUtils.findTodoById(todoRepository, todoId);
		List<Onboarding> todoTakes = todo.getTakes().stream()
			.map(Take::getOnboarding)
			.sorted(Onboarding::compareTo)
			.collect(Collectors.toList());
		List<Onboarding> meFirstTodoTakes = UserServiceUtils.toMeFirstList(todoTakes, user.getOnboarding());
		List<UserPersonalityInfo> userPersonalityInfos = TodoServiceUtils.toUserPersonalityInfoList(meFirstTodoTakes);
		return TodoSummaryInfoResponse.of(todo, userPersonalityInfos, user.getOnboarding());
	}

	public TodoFilterResponse getTodosByFilter(List<DayOfWeek> dayOfWeeks, List<Long> onboardingIds, Long userId) {
		User user = UserServiceUtils.findUserById(userRepository, userId);
		Room room = RoomServiceUtils.findParticipatingRoom(user);
		List<Todo> todos = room.getTodos();
		todos = TodoServiceUtils.filterByDayOfWeeks(todos, dayOfWeeks);
		todos = TodoServiceUtils.filterByOnboardingIds(todos, onboardingIds);
		todos.sort(Comparator.comparing(AuditingTimeEntity::getCreatedAt));
		return TodoFilterResponse.of(todos, DateUtils.todayLocalDateTime());
	}

	public MyTodoInfoResponse getMyTodoInfo(Long userId) {
		User user = UserServiceUtils.findUserById(userRepository, userId);
		Room room = RoomServiceUtils.findParticipatingRoom(user);
		List<Todo> todos = room.getTodos();
		List<Todo> myTodos = TodoServiceUtils.filterAllDaysUserTodos(todos, user.getOnboarding()).stream()
			.filter(todo -> todo.getTakes().size() == 1)
			.sorted(Comparator.comparing(AuditingTimeEntity::getCreatedAt))
			.collect(Collectors.toList());
		return MyTodoInfoResponse.of(myTodos, user.getOnboarding());
	}
}
