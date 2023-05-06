package hous.api.service.todo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hous.api.service.room.RoomServiceUtils;
import hous.api.service.todo.dto.response.DayOfWeekTodo;
import hous.api.service.todo.dto.response.MyTodoInfoResponse;
import hous.api.service.todo.dto.response.OurTodo;
import hous.api.service.todo.dto.response.OurTodoInfo;
import hous.api.service.todo.dto.response.TodoAllDayInfo;
import hous.api.service.todo.dto.response.TodoAllDayResponse;
import hous.api.service.todo.dto.response.TodoAllMemberInfo;
import hous.api.service.todo.dto.response.TodoAllMemberResponse;
import hous.api.service.todo.dto.response.TodoDetailInfo;
import hous.api.service.todo.dto.response.TodoInfo;
import hous.api.service.todo.dto.response.TodoInfoResponse;
import hous.api.service.todo.dto.response.TodoMainResponse;
import hous.api.service.todo.dto.response.TodoSummaryInfoResponse;
import hous.api.service.todo.dto.response.UserPersonalityInfo;
import hous.api.service.todo.dto.response.UserPersonalityInfoResponse;
import hous.api.service.user.UserServiceUtils;
import hous.common.util.DateUtils;
import hous.core.domain.common.AuditingTimeEntity;
import hous.core.domain.personality.PersonalityColor;
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

	public TodoAllDayResponse getTodoAllDayInfo(Long userId) {
		User user = UserServiceUtils.findUserById(userRepository, userId);
		Onboarding onboarding = user.getOnboarding();
		Room room = RoomServiceUtils.findParticipatingRoom(user);

		// 이 방의 모든 요일의 todo list 조회
		List<Todo> ourTodosList = room.getTodos();
		List<Todo> myTodosList = TodoServiceUtils.filterAllDaysUserTodos(ourTodosList, onboarding);

		// 요일별(index) todo list 형태로 가공
		Map<Integer, Set<Todo>> allDayOurTodosList = TodoServiceUtils.mapByDayOfWeekToOurTodosList(ourTodosList);
		Map<Integer, Set<Todo>> allDayMyTodosList = TodoServiceUtils.mapByDayOfWeekToMyTodosList(onboarding,
			myTodosList);

		// List<TodoAllDayResponse> response dto 형태로 가공
		List<TodoAllDayInfo> allDayTodosList = new ArrayList<>();
		for (int day = DayOfWeek.MONDAY.getIndex(); day <= DayOfWeek.SUNDAY.getIndex(); day++) {
			String dayOfWeek = DayOfWeek.getValueByIndex(day);
			List<TodoInfo> todoInfos = allDayMyTodosList.get(day).stream()
				.sorted(Comparator.comparing(AuditingTimeEntity::getCreatedAt))
				.map(todo -> TodoInfo.of(todo.getId(), todo.getName()))
				.collect(Collectors.toList());
			List<OurTodo> ourTodoInfos = allDayOurTodosList.get(day).stream()
				.sorted(Comparator.comparing(AuditingTimeEntity::getCreatedAt))
				.map(todo -> OurTodo.of(todo.getId(), todo.getName(), todo.getTakes().stream()
					.map(Take::getOnboarding)
					.collect(Collectors.toSet()), onboarding))
				.collect(Collectors.toList());
			allDayTodosList.add(TodoAllDayInfo.of(dayOfWeek, todoInfos, ourTodoInfos));
		}
		return TodoAllDayResponse.of(room.getTodos().size(), allDayTodosList);
	}

	public TodoAllMemberResponse getTodoAllMemberInfo(Long userId) {
		User user = UserServiceUtils.findUserById(userRepository, userId);
		Room room = RoomServiceUtils.findParticipatingRoom(user);
		List<Todo> todos = room.getTodos();

		List<TodoAllMemberInfo> allMemberTodos = new ArrayList<>();
		List<TodoAllMemberInfo> otherMemberTodos = new ArrayList<>();

		// 성향테스트 참여 순서로 정렬
		room.getParticipates().stream()
			.sorted(Participate::compareTo)
			.forEach(participate -> {
				Onboarding onboarding = participate.getOnboarding();
				List<Todo> memberTodos = TodoServiceUtils.filterAllDaysUserTodos(todos, onboarding);

				Map<Integer, Set<Todo>> allDayMemberTodos = TodoServiceUtils.mapByDayOfWeekToMyTodosList(onboarding,
					memberTodos);

				List<DayOfWeekTodo> dayOfWeekTodos = new ArrayList<>();
				for (int day = DayOfWeek.MONDAY.getIndex(); day <= DayOfWeek.SUNDAY.getIndex(); day++) {
					String dayOfWeek = DayOfWeek.getValueByIndex(day);
					List<TodoInfo> thisDayTodosName = allDayMemberTodos.get(day).stream()
						.sorted(Comparator.comparing(AuditingTimeEntity::getCreatedAt))
						.map(todo -> TodoInfo.of(todo.getId(), todo.getName()))
						.collect(Collectors.toList());
					dayOfWeekTodos.add(DayOfWeekTodo.of(dayOfWeek, thisDayTodosName.size(), thisDayTodosName));
				}

				String userName = onboarding.getNickname();
				PersonalityColor color = onboarding.getPersonality().getColor();

				if (user.getOnboarding().equals(onboarding)) {
					allMemberTodos.add(TodoAllMemberInfo.of(userName, color, memberTodos.size(), dayOfWeekTodos));
				} else {
					otherMemberTodos.add(TodoAllMemberInfo.of(userName, color, memberTodos.size(), dayOfWeekTodos));
				}
			});
		allMemberTodos.addAll(otherMemberTodos);

		return TodoAllMemberResponse.of(room.getTodos().size(), allMemberTodos);
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
