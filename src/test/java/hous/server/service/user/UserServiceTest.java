package hous.server.service.user;

import hous.server.domain.badge.mysql.AcquireRepository;
import hous.server.domain.badge.mysql.RepresentRepository;
import hous.server.domain.feedback.FeedbackType;
import hous.server.domain.notification.mysql.NotificationRepository;
import hous.server.domain.personality.mysql.PersonalityRepository;
import hous.server.domain.room.Room;
import hous.server.domain.room.mysql.ParticipateRepository;
import hous.server.domain.room.mysql.RoomRepository;
import hous.server.domain.rule.mysql.RuleRepository;
import hous.server.domain.todo.mysql.RedoRepository;
import hous.server.domain.todo.mysql.TakeRepository;
import hous.server.domain.todo.mysql.TodoRepository;
import hous.server.domain.user.User;
import hous.server.domain.user.UserSocialType;
import hous.server.domain.user.mysql.OnboardingRepository;
import hous.server.domain.user.mysql.TestScoreRepository;
import hous.server.domain.user.mysql.UserRepository;
import hous.server.service.room.RoomService;
import hous.server.service.user.dto.request.CreateUserRequestDto;
import hous.server.service.user.dto.request.DeleteUserRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles(value = "local")
public class UserServiceTest {

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
    private RepresentRepository representRepository;

    @Autowired
    private TestScoreRepository testScoreRepository;

    @Autowired
    private PersonalityRepository personalityRepository;

    @Autowired
    private RuleRepository ruleRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private RoomService roomService;

    @BeforeEach
    public void reset() {
        redoRepository.deleteAllInBatch();
        takeRepository.deleteAllInBatch();
        todoRepository.deleteAllInBatch();
        participateRepository.deleteAllInBatch();
        acquireRepository.deleteAllInBatch();
        ruleRepository.deleteAllInBatch();
        representRepository.deleteAllInBatch();
        testScoreRepository.deleteAllInBatch();
        roomRepository.deleteAllInBatch();
        notificationRepository.deleteAllInBatch();
        onboardingRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    // 정책상 방에 들어가지 않을 경우에만 사용자 삭제가 가능
    @Test
    @DisplayName("방에 들어가지 않은 사용자 삭제 성공")
    public void delete_user_by_not_join_room_success() {
        // given
        CreateUserRequestDto createUserRequestDto = CreateUserRequestDto.of(
                "socialId1", UserSocialType.KAKAO, "fcmToken1", "nickname1", "2022-01-01", true);
        Long userId = userService.registerUser(createUserRequestDto);
        DeleteUserRequestDto deleteUserRequestDto = DeleteUserRequestDto.of(FeedbackType.NO, "");

        // when
        userService.deleteUser(deleteUserRequestDto, userId);

        // then
        List<User> users = userRepository.findAll();
        assertThat(users).isEmpty();
        List<Room> rooms = roomRepository.findAll();
        assertThat(rooms).isEmpty();
    }
}
