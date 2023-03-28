package hous.server.service.room;

import hous.server.domain.badge.Represent;
import hous.server.domain.badge.mysql.RepresentRepository;
import hous.server.domain.notification.mongo.NotificationRepository;
import hous.server.domain.personality.PersonalityColor;
import hous.server.domain.personality.mysql.PersonalityRepository;
import hous.server.domain.room.Participate;
import hous.server.domain.room.Room;
import hous.server.domain.room.mysql.ParticipateRepository;
import hous.server.domain.room.mysql.RoomRepository;
import hous.server.domain.todo.DayOfWeek;
import hous.server.domain.todo.Redo;
import hous.server.domain.todo.Take;
import hous.server.domain.todo.Todo;
import hous.server.domain.todo.mysql.RedoRepository;
import hous.server.domain.todo.mysql.TakeRepository;
import hous.server.domain.todo.mysql.TodoRepository;
import hous.server.domain.user.Onboarding;
import hous.server.domain.user.TestScore;
import hous.server.domain.user.User;
import hous.server.domain.user.UserSocialType;
import hous.server.domain.user.mysql.OnboardingRepository;
import hous.server.domain.user.mysql.TestScoreRepository;
import hous.server.domain.user.mysql.UserRepository;
import hous.server.service.room.dto.request.SetRoomNameRequestDto;
import hous.server.service.todo.TodoService;
import hous.server.service.todo.dto.request.TodoInfoRequestDto;
import hous.server.service.user.UserService;
import hous.server.service.user.dto.request.CreateUserRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles(value = "local")
@Transactional
public class RoomServiceTest {

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
    private RepresentRepository representRepository;

    @Autowired
    private TestScoreRepository testScoreRepository;

    @Autowired
    private PersonalityRepository personalityRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private TodoService todoService;

    @Test
    @DisplayName("방에 들어가자마자 나가기 성공")
    public void leave_room_success() {
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
        roomService.leaveRoom(userId);

        // then
        List<Room> rooms = roomRepository.findAll();
        assertThat(rooms).isEmpty();

        // 방을 나간 후 내가 추가한 할일이 삭제되었는지 확인
        List<Todo> todos = todoRepository.findAll();
        List<Take> takes = takeRepository.findAll();
        List<Redo> redos = redoRepository.findAll();
        assertThat(todos).isEmpty();
        assertThat(takes).isEmpty();
        assertThat(redos).isEmpty();

        // 방을 나간 후 나의 정보들이 삭제되었는지 확인
        List<Onboarding> onboardings = onboardingRepository.findAll();
        assertThat(onboardings).hasSize(1);
        Onboarding onboarding = onboardings.get(0);

        List<Represent> represents = representRepository.findAll();
        assertThat(represents).isEmpty();
        List<TestScore> testScores = testScoreRepository.findAll();
        assertThat(testScores).isEmpty();
        assertThat(onboarding.getPersonality()).isEqualTo(personalityRepository.findPersonalityByColor(PersonalityColor.GRAY));
        assertThat(onboarding.isPublic()).isFalse();
        assertThat(onboarding.getMbti()).isNull();
        assertThat(onboarding.getJob()).isNull();
        assertThat(onboarding.getIntroduction()).isNull();
        assertThat(onboarding.getTestScore()).isNull();
        assertThat(onboarding.getRepresent()).isNull();
        assertThat(notificationRepository.countAllByOnboarding(onboarding)).isEqualTo(0);
    }

    @Test
    @DisplayName("방에 여러명 참여할 때 주인이 아닌 사람이 나가기 성공")
    public void leave_room_by_participate_success() {
        // given
        CreateUserRequestDto createUserRequestDto1 = CreateUserRequestDto.of(
                "socialId1", UserSocialType.KAKAO, "fcmToken1", "nickname1", "2022-01-01", true);
        Long owner = userService.registerUser(createUserRequestDto1);
        User ownerUser = userRepository.findUserById(owner);

        CreateUserRequestDto createUserRequestDto2 = CreateUserRequestDto.of(
                "socialId2", UserSocialType.KAKAO, "fcmToken2", "nickname2", "2022-01-01", true);
        Long userId2 = userService.registerUser(createUserRequestDto2);
        User user2 = userRepository.findUserById(userId2);

        CreateUserRequestDto createUserRequestDto3 = CreateUserRequestDto.of(
                "socialId3", UserSocialType.KAKAO, "fcmToken3", "nickname3", "2022-01-01", true);
        Long userId3 = userService.registerUser(createUserRequestDto3);

        SetRoomNameRequestDto setRoomNameRequestDto = SetRoomNameRequestDto.of("room1");
        Long roomId = roomService.createRoom(setRoomNameRequestDto, owner).getRoomId();
        roomService.joinRoom(roomId, userId2);
        roomService.joinRoom(roomId, userId3);

        TodoInfoRequestDto todoInfoRequestDto = TodoInfoRequestDto.of("todo1", true,
                List.of(TodoInfoRequestDto.TodoUser.builder()
                                .onboardingId(ownerUser.getOnboarding().getId())
                                .dayOfWeeks(List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY))
                                .build(),
                        TodoInfoRequestDto.TodoUser.builder()
                                .onboardingId(user2.getOnboarding().getId())
                                .dayOfWeeks(List.of(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY))
                                .build()));
        todoService.createTodo(todoInfoRequestDto, userId2);

        // when
        roomService.leaveRoom(userId2);

        // then
        List<Room> rooms = roomRepository.findAll();
        assertThat(rooms).hasSize(1);
        List<Participate> participates = participateRepository.findAll();
        assertThat(participates).hasSize(2);
        assertThat(owner).isEqualTo(rooms.get(0).getOwner().getUser().getId());

        // 방을 나간 후 나간 유저가(userId2) 담당자에서 빠졌는지 확인
        List<Todo> todos = todoRepository.findAll();
        List<Take> takes = takeRepository.findAll();
        List<Redo> redos = redoRepository.findAll();
        assertThat(todos).hasSize(1);
        assertThat(takes).hasSize(1);
        assertThat(redos).hasSize(3);
        assertThat(takes.get(0).getOnboarding().getUser().getId()).isEqualTo(owner);

        // 방을 나간 후 나의 정보들이 삭제되었는지 확인
        User user = userRepository.findUserById(userId2);
        Onboarding onboarding = user.getOnboarding();
        assertThat(onboarding.getPersonality()).isEqualTo(personalityRepository.findPersonalityByColor(PersonalityColor.GRAY));
        assertThat(onboarding.isPublic()).isFalse();
        assertThat(onboarding.getMbti()).isNull();
        assertThat(onboarding.getJob()).isNull();
        assertThat(onboarding.getIntroduction()).isNull();
        assertThat(onboarding.getTestScore()).isNull();
        assertThat(onboarding.getRepresent()).isNull();
        assertThat(notificationRepository.countAllByOnboarding(onboarding)).isEqualTo(0);
    }

    @Test
    @DisplayName("방에 여러명 참여할 때 주인이 나가기 성공 후 owner 넘겨주기 성공")
    public void leave_room_by_owner_success() {
        // given
        CreateUserRequestDto createUserRequestDto1 = CreateUserRequestDto.of(
                "socialId1", UserSocialType.KAKAO, "fcmToken1", "nickname1", "2022-01-01", true);
        Long owner = userService.registerUser(createUserRequestDto1);
        User ownerUser = userRepository.findUserById(owner);

        CreateUserRequestDto createUserRequestDto2 = CreateUserRequestDto.of(
                "socialId2", UserSocialType.KAKAO, "fcmToken2", "nickname2", "2022-01-01", true);
        Long userId2 = userService.registerUser(createUserRequestDto2);
        User user2 = userRepository.findUserById(userId2);

        CreateUserRequestDto createUserRequestDto3 = CreateUserRequestDto.of(
                "socialId3", UserSocialType.KAKAO, "fcmToken3", "nickname3", "2022-01-01", true);
        Long userId3 = userService.registerUser(createUserRequestDto3);

        SetRoomNameRequestDto setRoomNameRequestDto = SetRoomNameRequestDto.of("room1");
        Long roomId = roomService.createRoom(setRoomNameRequestDto, owner).getRoomId();
        roomService.joinRoom(roomId, userId2);
        roomService.joinRoom(roomId, userId3);

        TodoInfoRequestDto todoInfoRequestDto = TodoInfoRequestDto.of("todo1", true,
                List.of(TodoInfoRequestDto.TodoUser.builder()
                                .onboardingId(ownerUser.getOnboarding().getId())
                                .dayOfWeeks(List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY))
                                .build(),
                        TodoInfoRequestDto.TodoUser.builder()
                                .onboardingId(user2.getOnboarding().getId())
                                .dayOfWeeks(List.of(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY))
                                .build()));
        todoService.createTodo(todoInfoRequestDto, userId2);

        // when
        roomService.leaveRoom(owner);

        // then
        List<Room> rooms = roomRepository.findAll();
        assertThat(rooms).hasSize(1);
        List<Participate> participates = rooms.get(0).getParticipates();
        assertThat(participates).hasSize(2);
        assertThat(userId2).isEqualTo(rooms.get(0).getOwner().getUser().getId()); // 두번째로 들어간 사람이 주인이 된다.

        // 방을 나간 후 나간 유저가(owner) 담당자에서 빠졌는지 확인
        List<Todo> todos = todoRepository.findAll();
        List<Take> takes = takeRepository.findAll();
        List<Redo> redos = redoRepository.findAll();
        assertThat(todos).hasSize(1);
        assertThat(takes).hasSize(1);
        assertThat(redos).hasSize(2);
        assertThat(takes.get(0).getOnboarding().getUser().getId()).isEqualTo(userId2);

        // 방을 나간 후 나의 정보들이 삭제되었는지 확인
        User user = userRepository.findUserById(owner);
        Onboarding onboarding = user.getOnboarding();
        assertThat(onboarding.getPersonality()).isEqualTo(personalityRepository.findPersonalityByColor(PersonalityColor.GRAY));
        assertThat(onboarding.isPublic()).isFalse();
        assertThat(onboarding.getMbti()).isNull();
        assertThat(onboarding.getJob()).isNull();
        assertThat(onboarding.getIntroduction()).isNull();
        assertThat(onboarding.getTestScore()).isNull();
        assertThat(onboarding.getRepresent()).isNull();
        assertThat(notificationRepository.countAllByOnboarding(onboarding)).isEqualTo(0);
    }
}
