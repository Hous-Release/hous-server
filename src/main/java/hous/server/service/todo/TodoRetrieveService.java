package hous.server.service.todo;

import hous.server.common.util.DateUtils;
import hous.server.domain.common.AuditingTimeEntity;
import hous.server.domain.room.Participate;
import hous.server.domain.room.Room;
import hous.server.domain.todo.Todo;
import hous.server.domain.todo.repository.DoneRepository;
import hous.server.domain.todo.repository.TodoRepository;
import hous.server.domain.user.Onboarding;
import hous.server.domain.user.User;
import hous.server.domain.user.repository.UserRepository;
import hous.server.service.room.RoomServiceUtils;
import hous.server.service.todo.dto.response.*;
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
                .sorted(Comparator.comparing(onboarding -> onboarding.getTestScore().getCreatedAt()))
                .collect(Collectors.toList());
        return UserPersonalityInfoResponse.of(onboardings);
    }

    public TodoMainResponse getTodoMain(Long userId) {
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
                                .collect(Collectors.toList())))
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
                .sorted(Comparator.comparing(onboarding -> onboarding.getTestScore().getCreatedAt()))
                .collect(Collectors.toList());
        List<UserPersonalityInfo> userPersonalityInfos = TodoServiceUtils.toUserPersonalityInfoList(todo);
        return TodoInfoResponse.of(todo, userPersonalityInfos, onboardings);
    }

    public TodoSummaryInfoResponse getTodoSummaryInfo(Long todoId, Long userId) {
        User user = UserServiceUtils.findUserById(userRepository, userId);
        Todo todo = TodoServiceUtils.findTodoById(todoRepository, todoId);
        List<UserPersonalityInfo> userPersonalityInfos = TodoServiceUtils.toUserPersonalityInfoList(todo);
        return TodoSummaryInfoResponse.of(todo, userPersonalityInfos, user.getOnboarding());
    }
}
