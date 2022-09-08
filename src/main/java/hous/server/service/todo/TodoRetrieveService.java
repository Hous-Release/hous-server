package hous.server.service.todo;

import hous.server.common.util.DateUtils;
import hous.server.domain.common.AuditingTimeEntity;
import hous.server.domain.personality.PersonalityColor;
import hous.server.domain.room.Participate;
import hous.server.domain.room.Room;
import hous.server.domain.todo.DayOfWeek;
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
import java.util.ArrayList;
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
                .sorted(Comparator.comparing(onboarding -> onboarding.getTestScore().getUpdatedAt()))
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
        List<MyTodoInfo> todayMyTodos = todayMyTodosList.stream().sorted(Comparator.comparing(AuditingTimeEntity::getCreatedAt)).map(todo -> MyTodoInfo.of(todo.getId(), todo.getName(), doneRepository.findTodayTodoCheckStatus(today, user.getOnboarding(), todo))).collect(Collectors.toList());
        List<OurTodoInfo> todayOurTodos = todayOurTodosList.stream().sorted(Comparator.comparing(AuditingTimeEntity::getCreatedAt)).map(todo -> OurTodoInfo.of(todo.getName(), doneRepository.findTodayOurTodoStatus(today, todo), todo.getTakes().stream().map(take -> take.getOnboarding().getNickname()).collect(Collectors.toSet()))).collect(Collectors.toList());
        return TodoMainResponse.of(today, todayMyTodos, todayOurTodos);
    }

    public TodoInfoResponse getTodoInfo(Long todoId, Long userId) {
        User user = UserServiceUtils.findUserById(userRepository, userId);
        Room room = RoomServiceUtils.findParticipatingRoom(user);
        Todo todo = TodoServiceUtils.findTodoById(todoRepository, todoId);
        List<Participate> participates = room.getParticipates();
        List<Onboarding> onboardings = participates.stream()
                .map(Participate::getOnboarding)
                .sorted(Comparator.comparing(onboarding -> onboarding.getTestScore().getUpdatedAt()))
                .collect(Collectors.toList());
        List<UserPersonalityInfo> userPersonalityInfos = TodoServiceUtils.toUserPersonalityInfoList(todo);
        return TodoInfoResponse.of(todo, userPersonalityInfos, onboardings);
    }

    public TodoSummaryInfoResponse getTodoSummaryInfo(Long todoId, Long userId) {
        User user = UserServiceUtils.findUserById(userRepository, userId);
        RoomServiceUtils.findParticipatingRoom(user);
        Todo todo = TodoServiceUtils.findTodoById(todoRepository, todoId);
        List<UserPersonalityInfo> userPersonalityInfos = TodoServiceUtils.toUserPersonalityInfoList(todo);
        return TodoSummaryInfoResponse.of(todo, userPersonalityInfos, user.getOnboarding());
    }

    public List<TodoAllDayResponse> getTodoAllDayInfo(Long userId) {
        User user = UserServiceUtils.findUserById(userRepository, userId);
        Room room = RoomServiceUtils.findParticipatingRoom(user);
        List<Todo> todos = room.getTodos();

        // 이 방의 모든 요일의 todo list 조회
        List<Todo> ourTodosList = TodoServiceUtils.filterAllDaysOurTodos(todos);
        List<Todo> myTodosList = TodoServiceUtils.filterAllDaysUserTodos(todos, user.getOnboarding());

        // 요일별(index) todo list 형태로 가공
        List<Todo>[] allDayOurTodosList = TodoServiceUtils.mapByDayOfWeekToList(ourTodosList);
        List<Todo>[] allDayMyTodosList = TodoServiceUtils.mapByDayOfWeekToList(myTodosList);

        // List<TodoAllDayResponse> response dto 형태로 가공
        List<TodoAllDayResponse> allDayTodosList = new ArrayList<>();
        for (int i = 1; i < 8; i++) {
            String dayOfWeek = DayOfWeek.getValueByIndex(i);
            List<MyTodo> myTodoInfos = allDayMyTodosList[i].stream()
                    .sorted(Comparator.comparing(AuditingTimeEntity::getCreatedAt))
                    .map(todo -> MyTodo.of(todo.getId(), todo.getName()))
                    .collect(Collectors.toList());
            List<OurTodo> ourTodoInfos = allDayOurTodosList[i].stream()
                    .sorted(Comparator.comparing(AuditingTimeEntity::getCreatedAt))
                    .map(todo -> OurTodo.of(todo.getName(), todo.getTakes().stream()
                            .map(take -> take.getOnboarding().getNickname()).collect(Collectors.toSet())))
                    .collect(Collectors.toList());
            allDayTodosList.add(TodoAllDayResponse.of(dayOfWeek, myTodoInfos, ourTodoInfos));
        }
        return allDayTodosList;
    }

    public List<TodoAllMemberResponse> getTodoAllMemberInfo(Long userId) {
        User user = UserServiceUtils.findUserById(userRepository, userId);
        Room room = RoomServiceUtils.findParticipatingRoom(user);
        List<Todo> todos = room.getTodos();

        List<TodoAllMemberResponse> allMemberTodos = new ArrayList<>();
        List<TodoAllMemberResponse> otherMemberTodos = new ArrayList<>();

        // 성향테스트 참여 순서로 정렬
        room.getParticipates().stream().sorted(
                Comparator.comparing(participate -> participate.getOnboarding().getTestScore().getUpdatedAt())
        ).forEach(participate -> {
            List<Todo> memberTodos = TodoServiceUtils.filterAllDaysUserTodos(todos, participate.getOnboarding());

            List<Todo>[] allDayMemberTodos = TodoServiceUtils.mapByDayOfWeekToList(memberTodos);

            List<DayOfWeekTodo> dayOfWeekTodos = new ArrayList<>();
            int totalTodoCnt = 0;
            for (int i = 1; i < allDayMemberTodos.length; i++) {
                String dayOfWeek = DayOfWeek.getValueByIndex(i);
                List<String> thisDayTodosName = allDayMemberTodos[i].stream().map(Todo::getName).collect(Collectors.toList());
                dayOfWeekTodos.add(DayOfWeekTodo.of(dayOfWeek, thisDayTodosName.size(), thisDayTodosName));
                totalTodoCnt += thisDayTodosName.size();
            }

            String userName = participate.getOnboarding().getNickname();
            PersonalityColor color = participate.getOnboarding().getPersonality().getColor();

            if (user.getOnboarding().equals(participate.getOnboarding())) {
                allMemberTodos.add(TodoAllMemberResponse.of(userName, color, totalTodoCnt, dayOfWeekTodos));
            } else {
                otherMemberTodos.add(TodoAllMemberResponse.of(userName, color, totalTodoCnt, dayOfWeekTodos));
            }
        });
        allMemberTodos.addAll(otherMemberTodos);

        return allMemberTodos;
    }
}
