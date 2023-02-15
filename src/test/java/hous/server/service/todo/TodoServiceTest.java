package hous.server.service.todo;

import hous.server.domain.badge.repository.AcquireRepository;
import hous.server.domain.notification.repository.NotificationRepository;
import hous.server.domain.room.repository.ParticipateRepository;
import hous.server.domain.room.repository.RoomRepository;
import hous.server.domain.todo.DayOfWeek;
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
}
