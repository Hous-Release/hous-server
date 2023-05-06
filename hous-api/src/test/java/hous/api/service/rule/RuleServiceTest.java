package hous.api.service.rule;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import hous.api.service.room.RoomService;
import hous.api.service.room.dto.request.SetRoomNameRequestDto;
import hous.api.service.room.dto.response.RoomInfoResponse;
import hous.api.service.rule.dto.request.CreateRuleRequestDto;
import hous.api.service.rule.dto.request.DeleteRuleRequestDto;
import hous.api.service.user.UserService;
import hous.api.service.user.dto.request.CreateUserRequestDto;
import hous.common.exception.ConflictException;
import hous.common.exception.NotFoundException;
import hous.core.domain.rule.Rule;
import hous.core.domain.rule.mysql.RuleRepository;
import hous.core.domain.user.UserSocialType;

@SpringBootTest
@ActiveProfiles(value = "local")
public class RuleServiceTest {

	@Autowired
	private RuleRepository ruleRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private RoomService roomService;

	@Autowired
	private RuleService ruleService;

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
	@DisplayName("이미 존재하는 rule 과 같은 이름을 가진 rule 추가할 경우 409 예외 발생")
	@Transactional
	public void create_duplicate_rule_name_throw_by_conflict_exception() {
		// given
		CreateUserRequestDto createUserRequestDto1 = CreateUserRequestDto.of(
			"socialId1", UserSocialType.KAKAO, "fcmToken1", "nickname1", "2022-01-01", true);
		Long userId = userService.registerUser(createUserRequestDto1);

		SetRoomNameRequestDto setRoomNameRequestDto = SetRoomNameRequestDto.of("room1");
		Long roomId = roomService.createRoom(setRoomNameRequestDto, userId).getRoomId();

		CreateRuleRequestDto createRuleRequestDto = CreateRuleRequestDto.of(List.of("rule"));
		ruleService.createRule(createRuleRequestDto, userId);

		// when, then
		List<Rule> rules = ruleRepository.findAll();
		assertThat(rules.size()).isEqualTo(1);
		String matchedExceptionMessage = String.format("방 (%s) 에 이미 존재하는 ruleName (%s) 입니다.", roomId, "rule");
		assertThatThrownBy(() -> {
			ruleService.createRule(createRuleRequestDto, userId);
		}).isInstanceOf(ConflictException.class)
			.hasMessageContaining(matchedExceptionMessage);
	}

	@Test
	@DisplayName("rule 1개 삭제 테스트")
	@Transactional
	public void delete_rule_success() {
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
		ruleService.deleteRules(DeleteRuleRequestDto.of(deleteRuleIds), userId);

		// then
		List<Rule> rules = ruleRepository.findAll();
		assertThat(rules).isEmpty();
	}

	@Test
	@DisplayName("rule 여러개 삭제 테스트")
	@Transactional
	public void delete_rules_success() {
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
		ruleService.deleteRules(DeleteRuleRequestDto.of(deleteRuleIds), userId);

		// then
		List<Rule> rules = ruleRepository.findAll();
		assertThat(rules).isEmpty();
	}

	@Test
	@DisplayName("rule 삭제 시 존재하지 않는 rule_id일 경우 404 예외 발생")
	@Transactional
	public void delete_rules_throw_by_not_found_exception() {
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
			ruleService.deleteRules(DeleteRuleRequestDto.of(deleteRuleIds), userId);
		}).isInstanceOf(NotFoundException.class)
			.hasMessageContaining(matchedExceptionMessage);
	}
}
