package hous.api.service.badge;

import hous.api.service.room.RoomService;
import hous.api.service.room.dto.request.SetRoomNameRequestDto;
import hous.api.service.room.dto.response.RoomInfoResponse;
import hous.api.service.rule.RuleService;
import hous.api.service.rule.dto.request.CreateRuleRequestDto;
import hous.api.service.todo.TodoService;
import hous.api.service.todo.dto.request.TodoInfoRequestDto;
import hous.api.service.user.UserService;
import hous.api.service.user.dto.request.CreateUserRequestDto;
import hous.api.service.user.dto.request.UpdateTestScoreRequestDto;
import hous.api.service.user.dto.response.UpdatePersonalityColorResponse;
import hous.common.util.DateUtils;
import hous.core.domain.badge.Acquire;
import hous.core.domain.badge.BadgeCounter;
import hous.core.domain.badge.BadgeCounterType;
import hous.core.domain.badge.BadgeInfo;
import hous.core.domain.badge.mongo.BadgeCounterRepository;
import hous.core.domain.badge.mysql.AcquireRepository;
import hous.core.domain.badge.mysql.BadgeRepository;
import hous.core.domain.personality.PersonalityColor;
import hous.core.domain.rule.Rule;
import hous.core.domain.rule.mysql.RuleRepository;
import hous.core.domain.todo.DayOfWeek;
import hous.core.domain.todo.Todo;
import hous.core.domain.todo.mysql.TodoRepository;
import hous.core.domain.user.User;
import hous.core.domain.user.UserSocialType;
import hous.core.domain.user.mysql.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles(value = "local")
@Transactional
public class BadgeServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    TodoRepository todoRepository;

    @Autowired
    RuleRepository ruleRepository;

    @Autowired
    AcquireRepository acquireRepository;

    @Autowired
    BadgeRepository badgeRepository;

    @Autowired
    BadgeCounterRepository badgeCounterRepository;

    @Autowired
    UserService userService;

    @Autowired
    RoomService roomService;

    @Autowired
    BadgeService badgeService;

    @Autowired
    TodoService todoService;

    @Autowired
    RuleService ruleService;

    @BeforeEach
    public void setUp() {
        this.badgeCounterRepository.deleteAll();
    }

    @Test
    @DisplayName("두근두근 하우스 배지 획득")
    public void acquire_badge_by_pounding_house_on_success() {
        // given
        CreateUserRequestDto createUserRequestDto = CreateUserRequestDto.of(
                "socialId1", UserSocialType.KAKAO, "fcmToken1", "nickname1", "2022-01-01", true);
        Long userId = userService.registerUser(createUserRequestDto);
        User user = userRepository.findUserById(userId);
        SetRoomNameRequestDto setRoomNameRequestDto = SetRoomNameRequestDto.of("room1");

        // when
        roomService.createRoom(setRoomNameRequestDto, userId);

        // then
        List<Acquire> acquires = acquireRepository.findAllAcquireByOnboarding(user.getOnboarding());
        Acquire acquireBadge = acquires.stream()
                .filter(acquire -> BadgeInfo.POUNDING_HOUSE.equals(acquire.getBadge().getInfo()))
                .findAny()
                .orElse(null);
        assertThat(acquires).hasSize(1);
        assertThat(acquireBadge).isNotNull();
    }

    @Test
    @DisplayName("나 이런 사람이야 배지 획득")
    public void acquire_badge_by_i_am_such_a_person_on_success() {
        // given
        CreateUserRequestDto createUserRequestDto = CreateUserRequestDto.of(
                "socialId1", UserSocialType.KAKAO, "fcmToken1", "nickname1", "2022-01-01", true);
        Long userId = userService.registerUser(createUserRequestDto);
        User user = userRepository.findUserById(userId);
        SetRoomNameRequestDto setRoomNameRequestDto = SetRoomNameRequestDto.of("room1");
        roomService.createRoom(setRoomNameRequestDto, userId);
        UpdateTestScoreRequestDto updateTestScoreRequestDto = UpdateTestScoreRequestDto.of(3, 3, 3, 3, 3);

        // when
        UpdatePersonalityColorResponse updatePersonalityColorResponse = userService.updateUserTestScore(updateTestScoreRequestDto, user.getId());

        // then
        List<Acquire> acquires = acquireRepository.findAllAcquireByOnboarding(user.getOnboarding());
        Acquire acquireBadge = acquires.stream()
                .filter(acquire -> BadgeInfo.I_AM_SUCH_A_PERSON.equals(acquire.getBadge().getInfo()))
                .findAny()
                .orElse(null);
        assertThat(acquires).hasSize(3);
        assertThat(acquireBadge).isNotNull();
        assertThat(updatePersonalityColorResponse.getColor()).isEqualTo(PersonalityColor.YELLOW);

        BadgeCounter badgeCounter = badgeCounterRepository.findByUserIdAndCountType(userId, BadgeCounterType.TEST_SCORE_COMPLETE);
        assertThat(badgeCounter).isNotNull();
        assertThat(badgeCounter.getCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("우리집 호미들 배지 획득")
    public void acquire_badge_by_our_house_homies_on_success() {
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
        UpdateTestScoreRequestDto updateTestScoreRequestDto1 = UpdateTestScoreRequestDto.of(9, 9, 9, 9, 9);
        UpdateTestScoreRequestDto updateTestScoreRequestDto2 = UpdateTestScoreRequestDto.of(9, 9, 9, 6, 6);

        // when
        UpdatePersonalityColorResponse updatePersonalityColorResponse1 = userService.updateUserTestScore(updateTestScoreRequestDto1, user1.getId());
        UpdatePersonalityColorResponse updatePersonalityColorResponse2 = userService.updateUserTestScore(updateTestScoreRequestDto2, user2.getId());

        // then
        List<Acquire> acquiresByUser1 = acquireRepository.findAllAcquireByOnboarding(user1.getOnboarding());
        List<Acquire> acquiresByUser2 = acquireRepository.findAllAcquireByOnboarding(user2.getOnboarding());
        Acquire acquireBadge1 = acquiresByUser1.stream()
                .filter(acquire -> BadgeInfo.OUR_HOUSE_HOMIES.equals(acquire.getBadge().getInfo()))
                .findAny()
                .orElse(null);
        Acquire acquireBadge2 = acquiresByUser2.stream()
                .filter(acquire -> BadgeInfo.OUR_HOUSE_HOMIES.equals(acquire.getBadge().getInfo()))
                .findAny()
                .orElse(null);
        assertThat(acquiresByUser1).hasSize(3);
        assertThat(acquiresByUser2).hasSize(3);
        assertThat(acquireBadge1).isNotNull();
        assertThat(acquireBadge2).isNotNull();
        assertThat(updatePersonalityColorResponse1.getColor()).isEqualTo(PersonalityColor.GREEN);
        assertThat(updatePersonalityColorResponse2.getColor()).isEqualTo(PersonalityColor.PURPLE);
    }

    @Test
    @DisplayName("나도 날 모르겠어 배지 획득")
    public void acquire_badge_by_i_dont_even_know_me_on_success() {
        // given
        CreateUserRequestDto createUserRequestDto = CreateUserRequestDto.of(
                "socialId1", UserSocialType.KAKAO, "fcmToken1", "nickname1", "2022-01-01", true);
        Long userId = userService.registerUser(createUserRequestDto);
        User user = userRepository.findUserById(userId);
        SetRoomNameRequestDto setRoomNameRequestDto = SetRoomNameRequestDto.of("room1");
        roomService.createRoom(setRoomNameRequestDto, userId);

        // when
        UpdatePersonalityColorResponse updatePersonalityColorResponse = null;
        for (int i = 0; i < 5; i++) {
            int randomNumber = (int) ((Math.random() * 3) + 3); // 3 ~ 9 까지의 랜덤 숫자
            updatePersonalityColorResponse = userService.updateUserTestScore(
                    UpdateTestScoreRequestDto.of(randomNumber, randomNumber, randomNumber, randomNumber, randomNumber),
                    userId
            );
        }
        // then
        List<Acquire> acquiresByUser = acquireRepository.findAllAcquireByOnboarding(user.getOnboarding());
        Acquire acquireBadge = acquiresByUser.stream()
                .filter(acquire -> BadgeInfo.I_DONT_EVEN_KNOW_ME.equals(acquire.getBadge().getInfo()))
                .findAny()
                .orElse(null);
        assertThat(acquiresByUser).hasSize(4);
        assertThat(acquireBadge).isNotNull();
        assertThat(user.getOnboarding().getPersonality().getColor()).isEqualTo(updatePersonalityColorResponse.getColor());

        BadgeCounter badgeCounter = badgeCounterRepository.findByUserIdAndCountType(userId, BadgeCounterType.TEST_SCORE_COMPLETE);
        assertThat(badgeCounter).isNull(); // 배지를 받으면 삭제되니까 데이터가 없어야 해
    }

    @Test
    @DisplayName("호미의 탄생 배지 획득")
    public void acquire_badge_by_homie_is_born_on_success() {
        // given
        String now = DateUtils.todayLocalDateToString();
        CreateUserRequestDto createUserRequestDto = CreateUserRequestDto.of(
                "socialId1", UserSocialType.KAKAO, "fcmToken1", "nickname1", now, true);
        Long userId = userService.registerUser(createUserRequestDto);
        User user = userRepository.findUserById(userId);
        SetRoomNameRequestDto setRoomNameRequestDto = SetRoomNameRequestDto.of("room1");
        roomService.createRoom(setRoomNameRequestDto, userId);

        // when
        badgeService.acquireBadge(user, BadgeInfo.HOMIE_IS_BORN);

        // then
        List<Acquire> acquiresByUser = acquireRepository.findAllAcquireByOnboarding(user.getOnboarding());
        Acquire acquireBadge = acquiresByUser.stream()
                .filter(acquire -> BadgeInfo.HOMIE_IS_BORN.equals(acquire.getBadge().getInfo()))
                .findAny()
                .orElse(null);
        assertThat(acquiresByUser).hasSize(2);
        assertThat(acquireBadge).isNotNull();
    }

    @Test
    @DisplayName("to-do 한 걸음 배지 획득")
    public void acquire_badge_by_todo_one_step_on_success() {
        // given
        String now = DateUtils.todayLocalDateToString();
        CreateUserRequestDto createUserRequestDto = CreateUserRequestDto.of(
                "socialId1", UserSocialType.KAKAO, "fcmToken1", "nickname1", now, true);
        Long userId = userService.registerUser(createUserRequestDto);
        User user = userRepository.findUserById(userId);
        SetRoomNameRequestDto setRoomNameRequestDto = SetRoomNameRequestDto.of("room1");
        roomService.createRoom(setRoomNameRequestDto, userId);

        // when
        TodoInfoRequestDto todoInfoRequestDto = TodoInfoRequestDto.of("todo1", true,
                List.of(TodoInfoRequestDto.TodoUser.builder()
                        .onboardingId(user.getOnboarding().getId())
                        .dayOfWeeks(List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY))
                        .build()));
        todoService.createTodo(todoInfoRequestDto, userId);

        // then
        List<Todo> todos = todoRepository.findAll();
        Todo todo = todos.stream().filter(t -> t.getName().equals("todo1")).findAny().orElse(null);
        assertThat(todos).hasSize(1);
        assertThat(todo).isNotNull();
        List<Acquire> acquiresByUser = acquireRepository.findAllAcquireByOnboarding(user.getOnboarding());
        Acquire acquireBadge = acquiresByUser.stream()
                .filter(acquire -> BadgeInfo.TODO_ONE_STEP.equals(acquire.getBadge().getInfo()))
                .findAny()
                .orElse(null);
        assertThat(acquiresByUser).hasSize(2);
        assertThat(acquireBadge).isNotNull();
    }

    // TODO 참 잘했어요 테스트
    // TODO 성실왕 호미 테스트
    // TODO todo-마스터 테스트

    @Test
    @DisplayName("기둥을 세우자 배지 획득")
    public void acquire_badge_by_lets_build_a_pole_on_success() {
        // given
        String now = DateUtils.todayLocalDateToString();
        CreateUserRequestDto createUserRequestDto = CreateUserRequestDto.of(
                "socialId1", UserSocialType.KAKAO, "fcmToken1", "nickname1", now, true);
        Long userId = userService.registerUser(createUserRequestDto);
        User user = userRepository.findUserById(userId);
        SetRoomNameRequestDto setRoomNameRequestDto = SetRoomNameRequestDto.of("room1");
        roomService.createRoom(setRoomNameRequestDto, userId);

        // when
        CreateRuleRequestDto createRuleRequestDto1 = CreateRuleRequestDto.of(List.of("rule1"));
        ruleService.createRule(createRuleRequestDto1, userId);

        // then
        List<Acquire> acquiresByUser = acquireRepository.findAllAcquireByOnboarding(user.getOnboarding());
        Acquire acquireBadge = acquiresByUser.stream()
                .filter(acquire -> BadgeInfo.LETS_BUILD_A_POLE.equals(acquire.getBadge().getInfo()))
                .findAny()
                .orElse(null);
        assertThat(acquiresByUser).hasSize(2);
        assertThat(acquireBadge).isNotNull();

        BadgeCounter badgeCounter = badgeCounterRepository.findByUserIdAndCountType(userId, BadgeCounterType.RULE_CREATE);
        assertThat(badgeCounter).isNotNull();
        assertThat(badgeCounter.getCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("우리집 기둥 호미 배지 획득")
    public void acquire_badge_by_our_house_pillar_homie_on_success() {
        // given
        String now = DateUtils.todayLocalDateToString();
        CreateUserRequestDto createUserRequestDto = CreateUserRequestDto.of(
                "socialId1", UserSocialType.KAKAO, "fcmToken1", "nickname1", now, true);
        Long userId = userService.registerUser(createUserRequestDto);
        User user = userRepository.findUserById(userId);
        SetRoomNameRequestDto setRoomNameRequestDto = SetRoomNameRequestDto.of("room1");
        roomService.createRoom(setRoomNameRequestDto, userId);

        // when
        for (int i = 0; i < 5; i++) {
            ruleService.createRule(CreateRuleRequestDto.of(List.of(String.valueOf(i))), userId);
        }

        // then
        List<Rule> rules = ruleRepository.findAll();
        IntStream.range(0, 5).mapToObj(index -> assertThat(rules.get(index).getName()).isEqualTo(index));
        assertThat(rules).hasSize(5);
        List<Acquire> acquiresByUser = acquireRepository.findAllAcquireByOnboarding(user.getOnboarding());
        Acquire acquireBadge = acquiresByUser.stream()
                .filter(acquire -> BadgeInfo.OUR_HOUSE_PILLAR_HOMIE.equals(acquire.getBadge().getInfo()))
                .findAny()
                .orElse(null);
        assertThat(acquiresByUser).hasSize(3);
        assertThat(acquireBadge).isNotNull();

        BadgeCounter badgeCounter = badgeCounterRepository.findByUserIdAndCountType(userId, BadgeCounterType.RULE_CREATE);
        assertThat(badgeCounter).isNull(); // 배지를 받으면 삭제되니까 데이터가 없어야 해
    }

    @Test
    @DisplayName("피드백 한걸음 배지 획득")
    public void acquire_badge_by_feedback_one_step_on_success() {
        // given
        String now = DateUtils.todayLocalDateToString();
        CreateUserRequestDto createUserRequestDto = CreateUserRequestDto.of(
                "socialId1", UserSocialType.KAKAO, "fcmToken1", "nickname1", now, true);
        Long userId = userService.registerUser(createUserRequestDto);
        User user = userRepository.findUserById(userId);
        SetRoomNameRequestDto setRoomNameRequestDto = SetRoomNameRequestDto.of("room1");
        roomService.createRoom(setRoomNameRequestDto, userId);

        // when
        userService.acquireFeedbackBadge(userId);

        // then
        List<Acquire> acquiresByUser = acquireRepository.findAllAcquireByOnboarding(user.getOnboarding());
        Acquire acquireBadge = acquiresByUser.stream()
                .filter(acquire -> BadgeInfo.FEEDBACK_ONE_STEP.equals(acquire.getBadge().getInfo()))
                .findAny()
                .orElse(null);
        assertThat(acquiresByUser).hasSize(2);
        assertThat(acquireBadge).isNotNull();
    }


    @Test
    @DisplayName("나도 날 모르겠어 배지 마획득 - 카운터 테스트")
    public void acquire_badge_by_i_dont_even_know_me_on_fail() {
        // given
        CreateUserRequestDto createUserRequestDto = CreateUserRequestDto.of(
                "socialId1", UserSocialType.KAKAO, "fcmToken1", "nickname1", "2022-01-01", true);
        Long userId = userService.registerUser(createUserRequestDto);
        User user = userRepository.findUserById(userId);
        SetRoomNameRequestDto setRoomNameRequestDto = SetRoomNameRequestDto.of("room1");
        roomService.createRoom(setRoomNameRequestDto, userId);

        // when
        UpdatePersonalityColorResponse updatePersonalityColorResponse = null;
        for (int i = 0; i < 4; i++) {
            int randomNumber = (int) ((Math.random() * 3) + 3); // 3 ~ 9 까지의 랜덤 숫자
            updatePersonalityColorResponse = userService.updateUserTestScore(
                    UpdateTestScoreRequestDto.of(randomNumber, randomNumber, randomNumber, randomNumber, randomNumber),
                    userId
            );
        }
        // then
        List<Acquire> acquiresByUser = acquireRepository.findAllAcquireByOnboarding(user.getOnboarding());
        Acquire acquireBadge = acquiresByUser.stream()
                .filter(acquire -> BadgeInfo.I_DONT_EVEN_KNOW_ME.equals(acquire.getBadge().getInfo()))
                .findAny()
                .orElse(null);
        assertThat(acquiresByUser).hasSize(3);
        assertThat(acquireBadge).isNull();
        assertThat(user.getOnboarding().getPersonality().getColor()).isEqualTo(updatePersonalityColorResponse.getColor());

        BadgeCounter badgeCounter = badgeCounterRepository.findByUserIdAndCountType(userId, BadgeCounterType.TEST_SCORE_COMPLETE);
        assertThat(badgeCounter.getCount()).isEqualTo(4);
    }

    @Test
    @DisplayName("우리집 기둥 호미 배지 미획득 - 카운터 테스트")
    public void acquire_badge_by_our_house_pillar_homie_on_fail() {
        // given
        String now = DateUtils.todayLocalDateToString();
        CreateUserRequestDto createUserRequestDto = CreateUserRequestDto.of(
                "socialId1", UserSocialType.KAKAO, "fcmToken1", "nickname1", now, true);
        Long userId = userService.registerUser(createUserRequestDto);
        User user = userRepository.findUserById(userId);
        SetRoomNameRequestDto setRoomNameRequestDto = SetRoomNameRequestDto.of("room1");
        roomService.createRoom(setRoomNameRequestDto, userId);

        // when
        for (int i = 0; i < 3; i++) {
            ruleService.createRule(CreateRuleRequestDto.of(List.of(String.valueOf(i))), userId);
        }

        // then
        List<Rule> rules = ruleRepository.findAll();
        IntStream.range(0, 5).mapToObj(index -> assertThat(rules.get(index).getName()).isEqualTo(index));
        assertThat(rules).hasSize(3);

        List<Acquire> acquiresByUser = acquireRepository.findAllAcquireByOnboarding(user.getOnboarding());
        Acquire acquireBadge = acquiresByUser.stream()
                .filter(acquire -> BadgeInfo.OUR_HOUSE_PILLAR_HOMIE.equals(acquire.getBadge().getInfo()))
                .findAny()
                .orElse(null);
        assertThat(acquiresByUser).hasSize(2);
        assertThat(acquireBadge).isNull();

        BadgeCounter badgeCounter = badgeCounterRepository.findByUserIdAndCountType(userId, BadgeCounterType.RULE_CREATE);
        assertThat(badgeCounter.getCount()).isEqualTo(3);
    }
}
