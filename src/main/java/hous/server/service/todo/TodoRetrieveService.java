package hous.server.service.todo;

import hous.server.common.util.DateUtils;
import hous.server.domain.common.AuditingTimeEntity;
import hous.server.domain.personality.PersonalityColor;
import hous.server.domain.room.Participate;
import hous.server.domain.room.Room;
import hous.server.domain.todo.DayOfWeek;
import hous.server.domain.todo.Take;
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
                .sorted(Onboarding::compareTo)
                .collect(Collectors.toList());
        List<Onboarding> meFirstList = UserServiceUtils.toMeFirstList(onboardings, user.getOnboarding());
        return UserPersonalityInfoResponse.of(meFirstList);
    }

    public TodoMainResponse getTodoMain(Long userId) {
        User user = UserServiceUtils.findUserById(userRepository, userId);
        Room room = RoomServiceUtils.findParticipatingRoom(user);
        LocalDate today = DateUtils.todayLocalDate();
        List<Todo> todos = room.getTodos();
        List<Todo> todayOurTodosList = TodoServiceUtils.filterDayOurTodos(today, todos);
        List<Todo> todayMyTodosList = TodoServiceUtils.filterDayMyTodos(today, user.getOnboarding(), todos);
        List<TodoDetailInfo> todayMyTodos = todayMyTodosList.stream()
                .sorted(Comparator.comparing(AuditingTimeEntity::getCreatedAt))
                .map(todo -> TodoDetailInfo.of(todo.getId(), todo.getName(), doneRepository.findTodayTodoCheckStatus(today, user.getOnboarding(), todo)))
                .collect(Collectors.toList());
        List<OurTodoInfo> todayOurTodos = todayOurTodosList.stream()
                .sorted(Comparator.comparing(AuditingTimeEntity::getCreatedAt))
                .map(todo -> OurTodoInfo.of(todo.getName(), doneRepository.findTodayOurTodoStatus(today, todo), todo.getTakes().stream()
                        .map(take -> take.getOnboarding().getNickname())
                        .collect(Collectors.toSet())))
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
            List<TodoInfo> todoInfos = allDayMyTodosList[i].stream()
                    .sorted(Comparator.comparing(AuditingTimeEntity::getCreatedAt))
                    .map(todo -> TodoInfo.of(todo.getId(), todo.getName()))
                    .collect(Collectors.toList());
            List<OurTodo> ourTodoInfos = allDayOurTodosList[i].stream()
                    .sorted(Comparator.comparing(AuditingTimeEntity::getCreatedAt))
                    .map(todo -> OurTodo.of(todo.getName(), todo.getTakes().stream()
                            .map(take -> take.getOnboarding().getNickname()).collect(Collectors.toSet())))
                    .collect(Collectors.toList());
            allDayTodosList.add(TodoAllDayResponse.of(dayOfWeek, todoInfos, ourTodoInfos));
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
        room.getParticipates().stream()
                .sorted(Participate::compareTo)
                .forEach(participate -> {
                    List<Todo> memberTodos = TodoServiceUtils.filterAllDaysUserTodos(todos, participate.getOnboarding());

                    List<Todo>[] allDayMemberTodos = TodoServiceUtils.mapByDayOfWeekToList(memberTodos);

                    List<DayOfWeekTodo> dayOfWeekTodos = new ArrayList<>();
                    int totalTodoCnt = 0;
                    for (int i = 1; i < allDayMemberTodos.length; i++) {
                        String dayOfWeek = DayOfWeek.getValueByIndex(i);
                        List<TodoInfo> thisDayTodosName = allDayMemberTodos[i].stream()
                                .map(todo -> TodoInfo.of(todo.getId(), todo.getName()))
                                .collect(Collectors.toList());
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
