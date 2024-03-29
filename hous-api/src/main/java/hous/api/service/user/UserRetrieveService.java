package hous.api.service.user;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hous.api.service.room.RoomServiceUtils;
import hous.api.service.user.dto.response.MyBadgeInfoResponse;
import hous.api.service.user.dto.response.PersonalityInfoResponse;
import hous.api.service.user.dto.response.PersonalityTestInfoResponse;
import hous.api.service.user.dto.response.PushSettingResponse;
import hous.api.service.user.dto.response.UserInfoResponse;
import hous.core.domain.badge.Acquire;
import hous.core.domain.badge.Badge;
import hous.core.domain.badge.Represent;
import hous.core.domain.badge.mysql.AcquireRepository;
import hous.core.domain.badge.mysql.BadgeRepository;
import hous.core.domain.badge.mysql.RepresentRepository;
import hous.core.domain.personality.Personality;
import hous.core.domain.personality.PersonalityColor;
import hous.core.domain.personality.PersonalityTest;
import hous.core.domain.personality.mysql.PersonalityRepository;
import hous.core.domain.personality.mysql.PersonalityTestRepository;
import hous.core.domain.room.Room;
import hous.core.domain.user.Onboarding;
import hous.core.domain.user.User;
import hous.core.domain.user.mysql.OnboardingRepository;
import hous.core.domain.user.mysql.UserRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class UserRetrieveService {

	private final UserRepository userRepository;
	private final OnboardingRepository onboardingRepository;
	private final PersonalityRepository personalityRepository;
	private final PersonalityTestRepository personalityTestRepository;
	private final RepresentRepository representRepository;
	private final BadgeRepository badgeRepository;
	private final AcquireRepository acquireRepository;

	public UserInfoResponse getUserInfo(Long userId) {
		User user = UserServiceUtils.findUserById(userRepository, userId);
		RoomServiceUtils.findParticipatingRoom(user);
		return getProfileInfoByUser(user);
	}

	public UserInfoResponse getHomieInfo(Long homieOnboardingId, Long userId) {
		User user = UserServiceUtils.findUserById(userRepository, userId);
		Onboarding homieOnboarding = UserServiceUtils.findOnboardingById(onboardingRepository, homieOnboardingId);
		Room userRoom = RoomServiceUtils.findParticipatingRoom(user);
		Room homieRoom = RoomServiceUtils.findParticipatingRoom(homieOnboarding.getUser());
		RoomServiceUtils.checkParticipatingRoom(userRoom, homieRoom);
		return getProfileInfoByUser(homieOnboarding.getUser());
	}

	public PushSettingResponse getUserPushSetting(Long userId) {
		User user = UserServiceUtils.findUserById(userRepository, userId);
		return PushSettingResponse.of(user.getSetting());
	}

	public PersonalityInfoResponse getPersonalityInfo(PersonalityColor color) {
		UserServiceUtils.validatePersonalityColor(color);
		Personality personality = personalityRepository.findPersonalityByColor(color);
		return PersonalityInfoResponse.of(personality);
	}

	public List<PersonalityTestInfoResponse> getPersonalityTestInfo() {
		List<PersonalityTest> personalityTests = personalityTestRepository.findAllPersonalityTest();
		return personalityTests.stream()
			.map(PersonalityTestInfoResponse::of)
			.collect(Collectors.toList());
	}

	@Transactional
	public MyBadgeInfoResponse getMyBadgeList(Long userId) {
		User user = UserServiceUtils.findUserById(userRepository, userId);
		Onboarding onboarding = user.getOnboarding();
		RoomServiceUtils.findParticipatingRoom(user);
		Represent represent = onboarding.getRepresent();
		List<Badge> badges = badgeRepository.findAllBadge();
		List<Acquire> acquires = acquireRepository.findAllAcquireByOnboarding(onboarding);
		List<Badge> myBadges = acquires.stream()
			.map(Acquire::getBadge)
			.collect(Collectors.toList());
		List<Badge> newBadges = acquires.stream()
			.filter(acquire -> !acquire.isRead())
			.map(Acquire::getBadge)
			.collect(Collectors.toList());
		acquires.stream()
			.filter(acquire -> !acquire.isRead())
			.forEach(Acquire::updateIsRead);
		return MyBadgeInfoResponse.of(represent, badges, myBadges, newBadges);
	}

	private UserInfoResponse getProfileInfoByUser(User user) {
		Onboarding onboarding = user.getOnboarding();
		Represent represent = representRepository.findRepresentByOnboarding(onboarding);
		return UserInfoResponse.of(onboarding, represent);
	}
}
