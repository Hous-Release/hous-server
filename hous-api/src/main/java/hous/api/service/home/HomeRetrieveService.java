package hous.api.service.home;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hous.api.service.home.dto.response.HomeInfoResponse;
import hous.api.service.room.RoomServiceUtils;
import hous.api.service.todo.TodoServiceUtils;
import hous.api.service.todo.dto.response.OurTodoInfo;
import hous.api.service.todo.dto.response.TodoDetailInfo;
import hous.api.service.user.UserServiceUtils;
import hous.common.util.DateUtils;
import hous.core.domain.common.AuditingTimeEntity;
import hous.core.domain.room.Participate;
import hous.core.domain.room.Room;
import hous.core.domain.rule.Rule;
import hous.core.domain.todo.Take;
import hous.core.domain.todo.Todo;
import hous.core.domain.todo.mysql.DoneRepository;
import hous.core.domain.user.Onboarding;
import hous.core.domain.user.User;
import hous.core.domain.user.mysql.UserRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class HomeRetrieveService {

	private final UserRepository userRepository;
	private final DoneRepository doneRepository;

	public HomeInfoResponse getHomeInfo(Long userId) {
		User user = UserServiceUtils.findUserById(userRepository, userId);
		Onboarding onboarding = user.getOnboarding();
		Room room = RoomServiceUtils.findParticipatingRoom(user);
		LocalDate today = DateUtils.todayLocalDate();
		List<Todo> todos = room.getTodos();
		List<Todo> todayOurTodosList = TodoServiceUtils.filterDayOurTodos(today, todos);
		List<Todo> todayMyTodosList = TodoServiceUtils.filterDayMyTodos(today, onboarding, todos);
		List<TodoDetailInfo> todayMyTodos = todayMyTodosList.stream()
			.sorted(Comparator.comparing(AuditingTimeEntity::getCreatedAt))
			.map(todo -> TodoDetailInfo.of(
				todo.getId(),
				todo.getName(),
				doneRepository.findTodayTodoCheckStatus(today, onboarding, todo)))
			.collect(Collectors.toList());
		List<OurTodoInfo> todayOurTodos = todayOurTodosList.stream()
			.sorted(Comparator.comparing(AuditingTimeEntity::getCreatedAt))
			.map(todo -> OurTodoInfo.of(todo.getName(), doneRepository.findTodayOurTodoStatus(today, todo),
				todo.getTakes().stream()
					.map(Take::getOnboarding)
					.collect(Collectors.toSet()),
				onboarding))
			.collect(Collectors.toList());
		List<Rule> rules = room.getRules();
		List<Onboarding> participants = room.getParticipates().stream()
			.map(Participate::getOnboarding)
			.sorted(Onboarding::compareTo)
			.collect(Collectors.toList());
		List<Onboarding> meFirstList = UserServiceUtils.toMeFirstList(participants, onboarding);
		return HomeInfoResponse.of(onboarding, room, today, todayMyTodos, todayOurTodos, rules, meFirstList);
	}
}
