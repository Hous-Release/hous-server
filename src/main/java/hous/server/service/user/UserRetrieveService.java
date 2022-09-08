package hous.server.service.user;

import hous.server.domain.badge.Represent;
import hous.server.domain.badge.repository.RepresentRepository;
import hous.server.domain.personality.Personality;
import hous.server.domain.personality.PersonalityColor;
import hous.server.domain.personality.repository.PersonalityRepository;
import hous.server.domain.room.Room;
import hous.server.domain.room.repository.RoomRepository;
import hous.server.domain.user.Onboarding;
import hous.server.domain.user.User;
import hous.server.domain.user.repository.UserRepository;
import hous.server.service.room.RoomServiceUtils;
import hous.server.service.user.dto.response.CheckOnboardingInfoResponse;
import hous.server.service.user.dto.response.PersonalityInfoResponse;
import hous.server.service.user.dto.response.UserInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class UserRetrieveService {

    private final UserRepository userRepository;
    private final PersonalityRepository personalityRepository;
    private final RepresentRepository representRepository;
    private final RoomRepository roomRepository;

    public CheckOnboardingInfoResponse checkMyOnboardingInfo(Long userId) {
        User user = UserServiceUtils.findUserById(userRepository, userId);
        Onboarding onboarding = user.getOnboarding();
        return !onboarding.isChecked() ? CheckOnboardingInfoResponse.of(false) : CheckOnboardingInfoResponse.of(true);
    }

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

    private UserInfoResponse getProfileInfoByUser(User user) {
        Onboarding onboarding = user.getOnboarding();
        Represent represent = representRepository.findRepresentByOnboarding(onboarding);
        return UserInfoResponse.of(onboarding, represent);
    }
}
