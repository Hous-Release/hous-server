package hous.api.service.badge;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import hous.api.service.room.RoomService;
import hous.api.service.room.dto.request.SetRoomNameRequestDto;
import hous.api.service.user.UserService;
import hous.api.service.user.dto.request.CreateUserRequestDto;
import hous.common.exception.ForbiddenException;
import hous.common.exception.NotFoundException;
import hous.core.domain.badge.Badge;
import hous.core.domain.badge.BadgeInfo;
import hous.core.domain.badge.mysql.AcquireRepository;
import hous.core.domain.badge.mysql.BadgeRepository;
import hous.core.domain.user.User;
import hous.core.domain.user.UserSocialType;
import hous.core.domain.user.mysql.UserRepository;

@SpringBootTest
@ActiveProfiles(value = "local")
@Transactional
public class BadgeServiceUtilsTest {

	@Autowired
	UserRepository userRepository;

	@Autowired
	AcquireRepository acquireRepository;

	@Autowired
	BadgeRepository badgeRepository;

	@Autowired
	UserService userService;

	@Autowired
	RoomService roomService;

	@Test
	@DisplayName("배지 존재할 경우 true 를 반환")
	public void has_badge_exists_on_success() {
		// given
		CreateUserRequestDto createUserRequestDto = CreateUserRequestDto.of(
			"socialId1", UserSocialType.KAKAO, "fcmToken1", "nickname1", "2022-01-01", true);
		Long userId = userService.registerUser(createUserRequestDto);
		User user = userRepository.findUserById(userId);

		SetRoomNameRequestDto setRoomNameRequestDto = SetRoomNameRequestDto.of("room1");
		roomService.createRoom(setRoomNameRequestDto, userId);

		// when
		Boolean result = BadgeServiceUtils.hasBadge(badgeRepository, acquireRepository, BadgeInfo.POUNDING_HOUSE,
			user.getOnboarding());

		// then
		assertThat(result).isTrue();
	}

	@Test
	@DisplayName("배지가 존재하지 않을 경우 false 를 반환")
	public void has_badge_unexists_on_success() {
		// given
		CreateUserRequestDto createUserRequestDto = CreateUserRequestDto.of(
			"socialId1", UserSocialType.KAKAO, "fcmToken1", "nickname1", "2022-01-01", true);
		Long userId = userService.registerUser(createUserRequestDto);
		User user = userRepository.findUserById(userId);

		SetRoomNameRequestDto setRoomNameRequestDto = SetRoomNameRequestDto.of("room1");
		roomService.createRoom(setRoomNameRequestDto, userId);

		// when
		Boolean result = BadgeServiceUtils.hasBadge(badgeRepository, acquireRepository, BadgeInfo.I_AM_SUCH_A_PERSON,
			user.getOnboarding());

		// then
		assertThat(result).isFalse();
	}

	@Test
	@DisplayName("존재하는 배지의 경우 배지를 반환")
	public void find_badge_by_id_on_success() {
		// given
		BadgeInfo testBadgeInfo = BadgeInfo.I_AM_SUCH_A_PERSON;
		Badge testBadge = badgeRepository.findBadgeByBadgeInfo(testBadgeInfo);

		// when
		Badge resultBadge = BadgeServiceUtils.findBadgeById(badgeRepository, testBadge.getId());

		// then
		assertThat(resultBadge).isNotNull();
		assertThat(resultBadge.getInfo()).isEqualTo(testBadgeInfo);
	}

	@Test
	@DisplayName("존재하지 않는 배지의 경우 404 예외 발생")
	public void find_badge_by_id_throw_by_not_found_exception() {
		// given
		Long unExistsBadgeId = 50L;

		// when then
		String matchedExceptionMessage = String.format("존재하지 않는 badge (%s) 입니다", unExistsBadgeId);
		assertThatThrownBy(() -> {
			BadgeServiceUtils.findBadgeById(badgeRepository, unExistsBadgeId);
		}).isInstanceOf(NotFoundException.class)
			.hasMessageContaining(matchedExceptionMessage);
	}

	@Test
	@DisplayName("사용자가 획득한 배지가 아니라면 403 예외 반환")
	public void validate_exists_by_onboarding_and_badge_throw_by_forbidden_exception() {
		// given
		CreateUserRequestDto createUserRequestDto = CreateUserRequestDto.of(
			"socialId1", UserSocialType.KAKAO, "fcmToken1", "nickname1", "2022-01-01", true);
		Long userId = userService.registerUser(createUserRequestDto);
		User user = userRepository.findUserById(userId);

		Badge badge = badgeRepository.findBadgeByBadgeInfo(BadgeInfo.POUNDING_HOUSE);

		// when then
		String matchedExceptionMessage = String.format("유저가 (%s) 획득한 badge (%s) 가 아닙니다.", user.getOnboarding().getId(),
			badge.getId());
		assertThatThrownBy(() -> {
			BadgeServiceUtils.validateExistsByOnboardingAndBadge(acquireRepository, user.getOnboarding(), badge);
		}).isInstanceOf(ForbiddenException.class)
			.hasMessageContaining(matchedExceptionMessage);
	}
}
