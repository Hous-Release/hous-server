package hous.server.service.user;

import hous.server.domain.badge.Acquire;
import hous.server.domain.badge.Badge;
import hous.server.domain.badge.Represent;
import hous.server.domain.badge.repository.AcquireRepository;
import hous.server.domain.badge.repository.BadgeRepository;
import hous.server.domain.badge.repository.RepresentRepository;
import hous.server.domain.personality.Personality;
import hous.server.domain.personality.PersonalityColor;
import hous.server.domain.personality.PersonalityTest;
import hous.server.domain.personality.repository.PersonalityRepository;
import hous.server.domain.personality.repository.PersonalityTestRepository;
import hous.server.domain.room.Room;
import hous.server.domain.user.Onboarding;
import hous.server.domain.user.User;
import hous.server.domain.user.repository.UserRepository;
import hous.server.service.room.RoomServiceUtils;
import hous.server.service.user.dto.response.MyBadgeInfoResponse;
import hous.server.service.user.dto.response.PersonalityInfoResponse;
import hous.server.service.user.dto.response.PersonalityTestInfoResponse;
import hous.server.service.user.dto.response.UserInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class UserRetrieveService {

    private final UserRepository userRepository;
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

    public UserInfoResponse getHomieInfo(Long homieId, Long userId) {
        User user = UserServiceUtils.findUserById(userRepository, userId);
        User homie = UserServiceUtils.findUserById(userRepository, homieId);
        Room userRoom = RoomServiceUtils.findParticipatingRoom(user);
        Room homieRoom = RoomServiceUtils.findParticipatingRoom(homie);
        RoomServiceUtils.checkParticipatingRoom(userRoom, homieRoom);
        return getProfileInfoByUser(homie);
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
