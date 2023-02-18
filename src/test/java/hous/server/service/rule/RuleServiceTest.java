package hous.server.service.rule;

import hous.server.common.exception.NotFoundException;
import hous.server.domain.badge.repository.AcquireRepository;
import hous.server.domain.notification.repository.NotificationRepository;
import hous.server.domain.room.repository.ParticipateRepository;
import hous.server.domain.room.repository.RoomRepository;
import hous.server.domain.rule.Rule;
import hous.server.domain.rule.repository.RuleRepository;
import hous.server.domain.user.User;
import hous.server.domain.user.UserSocialType;
import hous.server.domain.user.repository.OnboardingRepository;
import hous.server.domain.user.repository.UserRepository;
import hous.server.service.room.RoomService;
import hous.server.service.room.dto.request.SetRoomNameRequestDto;
import hous.server.service.room.dto.response.RoomInfoResponse;
import hous.server.service.rule.dto.request.CreateRuleRequestDto;
import hous.server.service.rule.dto.request.DeleteRuleReqeustDto;
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
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles(value = "local")
public class RuleServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OnboardingRepository onboardingRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ParticipateRepository participateRepository;

    @Autowired
    private RuleRepository ruleRepository;

    @Autowired
    private AcquireRepository acquireRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private RuleService ruleService;

    @BeforeEach
    public void reset() {
        ruleRepository.deleteAllInBatch();
        participateRepository.deleteAllInBatch();
        acquireRepository.deleteAllInBatch();
        roomRepository.deleteAllInBatch();
        notificationRepository.deleteAllInBatch();
        onboardingRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("rule 동시에 2개 추가해도 성공")
    public void create_two_rules_at_once_success() throws InterruptedException {

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

        CreateRuleRequestDto createRuleRequestDto1 = CreateRuleRequestDto.of(List.of("rule1"));
        CreateRuleRequestDto createRuleRequestDto2 = CreateRuleRequestDto.of(List.of("rule2"));

        // when
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        CountDownLatch countDownLatch = new CountDownLatch(2);

        for (int i = 1; i <= 2; i++) {
            int finalI = i;
            executorService.execute(() -> {
                switch (finalI) {
                    case 1:
                        ruleService.createRule(createRuleRequestDto1, userId1);
                        break;
                    case 2:
                        ruleService.createRule(createRuleRequestDto2, userId2);
                        break;
                }
                countDownLatch.countDown();
            });
        }

        countDownLatch.await();

        // then
        List<Rule> rules = ruleRepository.findAll();
        assertThat(rules.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("rule 1게 삭제 테스트")
    public void delete_rule_test() {
        // given
        CreateUserRequestDto createUserRequestDto1 = CreateUserRequestDto.of(
                "socialId1", UserSocialType.KAKAO, "fcmToken1", "nickname1", "2022-01-01", true);
        Long userId = userService.registerUser(createUserRequestDto1);

        SetRoomNameRequestDto setRoomNameRequestDto = SetRoomNameRequestDto.of("room1");
        roomService.createRoom(setRoomNameRequestDto, userId);

        CreateRuleRequestDto createRuleRequestDto = CreateRuleRequestDto.of(List.of("rule1"));
        ruleService.createRule(createRuleRequestDto, userId);
        List<Long> deleteRuleIds = ruleRepository.findAll().stream().map(Rule::getId).collect(Collectors.toList());

        // when
        ruleService.deleteRules(DeleteRuleReqeustDto.of(deleteRuleIds), userId);

        // then
        List<Rule> rules = ruleRepository.findAll();
        assertThat(rules).isEmpty();
    }

    @Test
    @DisplayName("rule 여러게 삭제 테스트")
    public void delete_rules_test() {
        // given
        CreateUserRequestDto createUserRequestDto1 = CreateUserRequestDto.of(
                "socialId1", UserSocialType.KAKAO, "fcmToken1", "nickname1", "2022-01-01", true);
        Long userId = userService.registerUser(createUserRequestDto1);
        CreateRuleRequestDto createRuleRequestDto = CreateRuleRequestDto.of(List.of("rule1, rule2, rule3"));

        SetRoomNameRequestDto setRoomNameRequestDto = SetRoomNameRequestDto.of("room1");
        roomService.createRoom(setRoomNameRequestDto, userId);

        ruleService.createRule(createRuleRequestDto, userId);
        List<Long> deleteRuleIds = ruleRepository.findAll().stream().map(Rule::getId).collect(Collectors.toList());

        // when
        ruleService.deleteRules(DeleteRuleReqeustDto.of(deleteRuleIds), userId);

        // then
        List<Rule> rules = ruleRepository.findAll();
        assertThat(rules).isEmpty();
    }

    @Test
    @DisplayName("rule 삭제 시 존재하지 않는 rule_id일 경우 404 예외 발생")
    public void delete_rules_test_by_exception() {
        // given
        CreateUserRequestDto createUserRequestDto1 = CreateUserRequestDto.of(
                "socialId1", UserSocialType.KAKAO, "fcmToken1", "nickname1", "2022-01-01", true);
        Long userId = userService.registerUser(createUserRequestDto1);
        CreateRuleRequestDto createRuleRequestDto = CreateRuleRequestDto.of(List.of("rule1, rule2, rule3"));

        SetRoomNameRequestDto setRoomNameRequestDto = SetRoomNameRequestDto.of("room1");
        roomService.createRoom(setRoomNameRequestDto, userId);

        ruleService.createRule(createRuleRequestDto, userId);
        List<Long> deleteRuleIds = List.of(4L, 5L, 6L);

        // when, then
        String matchedExceptionMessage = String.format("존재하지 않는 규칙 (%s) 입니다", 4L);
        assertThatThrownBy(() -> {
            ruleService.deleteRules(DeleteRuleReqeustDto.of(deleteRuleIds), userId);
        }).isInstanceOf(NotFoundException.class)
                .hasMessageContaining(matchedExceptionMessage);
    }
}
