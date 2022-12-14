package hous.server.service.home;

import hous.server.common.util.DateUtils;
import hous.server.domain.common.AuditingTimeEntity;
import hous.server.domain.room.Participate;
import hous.server.domain.room.Room;
import hous.server.domain.rule.Rule;
import hous.server.domain.todo.Take;
import hous.server.domain.todo.Todo;
import hous.server.domain.todo.repository.DoneRepository;
import hous.server.domain.user.Onboarding;
import hous.server.domain.user.User;
import hous.server.domain.user.repository.UserRepository;
import hous.server.service.home.dto.response.HomeInfoResponse;
import hous.server.service.room.RoomServiceUtils;
import hous.server.service.todo.TodoServiceUtils;
import hous.server.service.todo.dto.response.OurTodoInfo;
import hous.server.service.todo.dto.response.TodoDetailInfo;
import hous.server.service.user.UserServiceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
