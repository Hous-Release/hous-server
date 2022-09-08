package hous.server.service.home;

import hous.server.common.util.DateUtils;
import hous.server.domain.common.AuditingTimeEntity;
import hous.server.domain.room.Participate;
import hous.server.domain.room.Room;
import hous.server.domain.rule.Rule;
import hous.server.domain.todo.Todo;
import hous.server.domain.todo.repository.DoneRepository;
import hous.server.domain.user.Onboarding;
import hous.server.domain.user.User;
import hous.server.domain.user.repository.UserRepository;
import hous.server.service.home.dto.response.HomeInfoResponse;
import hous.server.service.room.RoomServiceUtils;
import hous.server.service.todo.TodoServiceUtils;
import hous.server.service.todo.dto.response.MyTodoInfo;
import hous.server.service.todo.dto.response.OurTodoInfo;
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
        Room room = RoomServiceUtils.findParticipatingRoom(user);
        LocalDate today = DateUtils.today();
        List<Todo> todos = room.getTodos();
        List<Todo> todayOurTodosList = TodoServiceUtils.filterTodayOurTodos(today, todos);
        List<Todo> todayMyTodosList = TodoServiceUtils.filterTodayMyTodos(today, user.getOnboarding(), todos);
        List<MyTodoInfo> todayMyTodos = todayMyTodosList.stream()
                .sorted(Comparator.comparing(AuditingTimeEntity::getCreatedAt))
                .map(todo -> MyTodoInfo.of(
                        todo.getId(),
                        todo.getName(),
                        doneRepository.findTodayTodoCheckStatus(today, user.getOnboarding(), todo)))
                .collect(Collectors.toList());
        List<OurTodoInfo> todayOurTodos = todayOurTodosList.stream()
                .sorted(Comparator.comparing(AuditingTimeEntity::getCreatedAt))
                .map(todo -> OurTodoInfo.of(
                        todo.getName(),
                        doneRepository.findTodayOurTodoStatus(today, todo),
                        todo.getTakes().stream()
                                .map(take -> take.getOnboarding().getNickname())
                                .collect(Collectors.toSet())))
                .collect(Collectors.toList());
        List<Rule> rules = room.getRules();
        List<Onboarding> participants = room.getParticipates().stream()
                .map(Participate::getOnboarding)
                .sorted(Comparator.comparing(onboarding -> onboarding.getTestScore().getUpdatedAt()))
                .collect(Collectors.toList());
        return HomeInfoResponse.of(user.getOnboarding(), room, today, todayMyTodos, todayOurTodos, rules, participants);
    }
}
