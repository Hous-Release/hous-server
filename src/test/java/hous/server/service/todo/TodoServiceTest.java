package hous.server.service.todo;

import hous.server.common.exception.ConflictException;
import hous.server.common.exception.NotFoundException;
import hous.server.domain.badge.repository.AcquireRepository;
import hous.server.domain.notification.repository.NotificationRepository;
import hous.server.domain.room.repository.ParticipateRepository;
import hous.server.domain.room.repository.RoomRepository;
import hous.server.domain.rule.repository.RuleRepository;
import hous.server.domain.todo.DayOfWeek;
import hous.server.domain.todo.Redo;
import hous.server.domain.todo.Take;
import hous.server.domain.todo.Todo;
import hous.server.domain.todo.repository.RedoRepository;
import hous.server.domain.todo.repository.TakeRepository;
import hous.server.domain.todo.repository.TodoRepository;
import hous.server.domain.user.User;
import hous.server.domain.user.UserSocialType;
import hous.server.domain.user.repository.OnboardingRepository;
import hous.server.domain.user.repository.UserRepository;
import hous.server.service.room.RoomService;
import hous.server.service.room.dto.request.SetRoomNameRequestDto;
import hous.server.service.room.dto.response.RoomInfoResponse;
import hous.server.service.todo.dto.request.TodoInfoRequestDto;
import hous.server.service.user.UserService;
import hous.server.service.user.dto.request.CreateUserRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles(value = "local")
public class TodoServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OnboardingRepository onboardingRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ParticipateRepository participateRepository;

    @Autowired
    private RedoRepository redoRepository;

    @Autowired
    private TakeRepository takeRepository;

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private AcquireRepository acquireRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private RuleRepository ruleRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private TodoService todoService;

    @BeforeEach
    public void reset() {
        redoRepository.deleteAllInBatch();
        takeRepository.deleteAllInBatch();
        todoRepository.deleteAllInBatch();
        participateRepository.deleteAllInBatch();
        acquireRepository.deleteAllInBatch();
        ruleRepository.deleteAllInBatch();
        roomRepository.deleteAllInBatch();
        notificationRepository.deleteAllInBatch();
        onboardingRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("todo 동시에 2개 추가해도 성공")
    public void create_two_todos_at_once_success() throws InterruptedException {

        // given
        CreateUserRequestDto createUserRequestDto1 = CreateUserRequestDto.of(
                "socialId1", UserSocialType.KAKAO, "fcmToken1", "nickname1", "2022-01-01", true);
        CreateUserRequestDto createUserRequestDto2 = CreateUserRequestDto.of(
                "socialId2", UserSocialType.KAKAO, "fcmToken2", "nickname2", "2022-01-01", true);
        Long userId1 = userService.registerUser(createUserRequestDto1);
        Long userId2 = userService.registerUser(createUserRequestDto2);
        User user1 = userRepository.findUserById(userId1);
        User user2 = userRepository.findUserById(userId2);

        SetRoomNameRequestDto setRoomNameRequestDto = SetRoomNameRequestDto.of("room1");
        RoomInfoResponse roomInfoResponse = roomService.createRoom(setRoomNameRequestDto, userId1);
        roomService.joinRoom(roomInfoResponse.getRoomId(), userId2);

        TodoInfoRequestDto todoInfoRequestDto1 = TodoInfoRequestDto.of("todo1", true,
                List.of(TodoInfoRequestDto.TodoUser.builder()
                                .onboardingId(user1.getOnboarding().getId())
                                .dayOfWeeks(List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY))
                                .build(),
                        TodoInfoRequestDto.TodoUser.builder()
                                .onboardingId(user2.getOnboarding().getId())
                                .dayOfWeeks(List.of(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY))
                                .build()));
        TodoInfoRequestDto todoInfoRequestDto2 = TodoInfoRequestDto.of("todo1", true,
                List.of(TodoInfoRequestDto.TodoUser.builder()
                                .onboardingId(user1.getOnboarding().getId())
                                .dayOfWeeks(List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY))
                                .build(),
                        TodoInfoRequestDto.TodoUser.builder()
                                .onboardingId(user2.getOnboarding().getId())
                                .dayOfWeeks(List.of(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY))
                                .build()));

        // when
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        CountDownLatch countDownLatch = new CountDownLatch(2);

        for (int i = 1; i <= 2; i++) {
            int finalI = i;
            executorService.execute(() -> {
                switch (finalI) {
                    case 1:
                        todoService.createTodo(todoInfoRequestDto1, userId1);
                        break;
                    case 2:
                        todoService.createTodo(todoInfoRequestDto2, userId2);
                        break;
                }
                countDownLatch.countDown();
            });
        }

        countDownLatch.await();

        // then
        List<Todo> todos = todoRepository.findAll();
        assertThat(todos.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("이미 존재하는 rule 과 같은 이름을 가진 rule 추가할 경우 409 예외 발생")
    public void create_duplicate_rule_name_throw_by_conflit_exception() {
        // given
        CreateUserRequestDto createUserRequestDto = CreateUserRequestDto.of(
                "socialId1", UserSocialType.KAKAO, "fcmToken1", "nickname1", "2022-01-01", true);
        Long userId = userService.registerUser(createUserRequestDto);
        User user = userRepository.findUserById(userId);

        SetRoomNameRequestDto setRoomNameRequestDto = SetRoomNameRequestDto.of("room1");
        Long roomId = roomService.createRoom(setRoomNameRequestDto, userId).getRoomId();

        TodoInfoRequestDto todoInfoRequestDto = TodoInfoRequestDto.of("todo1", true,
                List.of(TodoInfoRequestDto.TodoUser.builder()
                        .onboardingId(user.getOnboarding().getId())
                        .dayOfWeeks(List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY))
                        .build()));
        todoService.createTodo(todoInfoRequestDto, userId);

        // when, then
        List<Todo> todos = todoRepository.findAll();
        List<Take> takes = takeRepository.findAll();
        List<Redo> redos = redoRepository.findAll();
        assertThat(todos.size()).isEqualTo(1);
        assertThat(takes.size()).isEqualTo(1);
        assertThat(redos.size()).isEqualTo(3);
        String matchedExceptionMessage = String.format("방 (%s) 에 이미 존재하는 todo (%s) 입니다.", roomId, "todo1");
        assertThatThrownBy(() -> {
            todoService.createTodo(todoInfoRequestDto, userId);
        }).isInstanceOf(ConflictException.class)
                .hasMessageContaining(matchedExceptionMessage);
    }

    @Test
    @DisplayName("todo 삭제 성공")
    public void delete_todo() {
        // given
        CreateUserRequestDto createUserRequestDto = CreateUserRequestDto.of(
                "socialId1", UserSocialType.KAKAO, "fcmToken1", "nickname1", "2022-01-01", true);
        Long userId = userService.registerUser(createUserRequestDto);
        User user = userRepository.findUserById(userId);

        SetRoomNameRequestDto setRoomNameRequestDto = SetRoomNameRequestDto.of("room1");
        roomService.createRoom(setRoomNameRequestDto, userId);

        TodoInfoRequestDto todoInfoRequestDto = TodoInfoRequestDto.of("todo1", true,
                List.of(TodoInfoRequestDto.TodoUser.builder()
                        .onboardingId(user.getOnboarding().getId())
                        .dayOfWeeks(List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY))
                        .build()));
        todoService.createTodo(todoInfoRequestDto, userId);


        // when
        Long todoId = todoRepository.findAll().get(0).getId();
        todoService.deleteTodo(todoId, userId);

        // then
        List<Todo> todos = todoRepository.findAll();
        List<Take> takes = takeRepository.findAll();
        List<Redo> redos = redoRepository.findAll();
        assertThat(todos).isEmpty();
        assertThat(takes).isEmpty();
        assertThat(redos).isEmpty();
    }

    @Test
    @DisplayName("todo 삭제 시 존재하지 않는 todo_id인 경우 404 예외 발생")
    public void delete_rules_test_by_exception() {
        // given
        CreateUserRequestDto createUserRequestDto = CreateUserRequestDto.of(
                "socialId1", UserSocialType.KAKAO, "fcmToken1", "nickname1", "2022-01-01", true);
        Long userId = userService.registerUser(createUserRequestDto);
        User user = userRepository.findUserById(userId);

        SetRoomNameRequestDto setRoomNameRequestDto = SetRoomNameRequestDto.of("room1");
        roomService.createRoom(setRoomNameRequestDto, userId);

        TodoInfoRequestDto todoInfoRequestDto = TodoInfoRequestDto.of("todo1", true,
                List.of(TodoInfoRequestDto.TodoUser.builder()
                        .onboardingId(user.getOnboarding().getId())
                        .dayOfWeeks(List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY))
                        .build()));
        todoService.createTodo(todoInfoRequestDto, userId);

        // when, then
        Long unExistsTodoId = todoRepository.findAll().size() + 1L;
        String matchedExceptionMessage = String.format("존재하지 않는 todo (%s) 입니다", unExistsTodoId);
        assertThatThrownBy(() -> {
            todoService.deleteTodo(2L, userId);
        }).isInstanceOf(NotFoundException.class)
                .hasMessageContaining(matchedExceptionMessage);
    }
}
