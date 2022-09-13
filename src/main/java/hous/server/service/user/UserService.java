package hous.server.service.user;

import hous.server.common.exception.ForbiddenException;
import hous.server.common.exception.NotFoundException;
import hous.server.domain.personality.Personality;
import hous.server.domain.personality.PersonalityColor;
import hous.server.domain.personality.repository.PersonalityRepository;
import hous.server.domain.room.Participate;
import hous.server.domain.user.Onboarding;
import hous.server.domain.user.Setting;
import hous.server.domain.user.TestScore;
import hous.server.domain.user.User;
import hous.server.domain.user.repository.OnboardingRepository;
import hous.server.domain.user.repository.SettingRepository;
import hous.server.domain.user.repository.TestScoreRepository;
import hous.server.domain.user.repository.UserRepository;
import hous.server.service.room.RoomServiceUtils;
import hous.server.service.user.dto.request.CreateUserDto;
import hous.server.service.user.dto.request.SetOnboardingInfoRequestDto;
import hous.server.service.user.dto.request.UpdateTestScoreRequestDto;
import hous.server.service.user.dto.request.UpdateUserInfoRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static hous.server.common.exception.ErrorCode.FORBIDDEN_USER_DELETE_ROOM_PARTICIPATE_EXCEPTION;
import static hous.server.common.exception.ErrorCode.NOTFOUND_USER_EXCEPTION;

@RequiredArgsConstructor
@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final OnboardingRepository onboardingRepository;
    private final SettingRepository settingRepository;
    private final TestScoreRepository testScoreRepository;
    private final PersonalityRepository personalityRepository;

    public Long registerUser(CreateUserDto request) {
        UserServiceUtils.validateNotExistsUser(userRepository, request.getSocialId(), request.getSocialType());
        User user = userRepository.save(User.newInstance(request.getSocialId(), request.getSocialType(), request.getFcmToken(),
                onboardingRepository.save(
                        Onboarding.newInstance(personalityRepository.findPersonalityByColor(PersonalityColor.GRAY),
                                testScoreRepository.save(TestScore.newInstance()))),
                settingRepository.save(Setting.newInstance())));
        return user.getId();
    }

    public void setOnboardingInfo(SetOnboardingInfoRequestDto request, Long userId) {
        User user = UserServiceUtils.findUserById(userRepository, userId);
        Onboarding onboarding = user.getOnboarding();
        onboarding.setOnboarding(request.getNickname(), request.getBirthday(), request.isPublic());
    }

    public void updateUserInfo(UpdateUserInfoRequestDto request, Long userId) {
        User user = UserServiceUtils.findUserById(userRepository, userId);
        RoomServiceUtils.findParticipatingRoom(user);
        Onboarding onboarding = user.getOnboarding();
        onboarding.setUserInfo(request);
    }

    // TODO 푸쉬알림 설정뷰 확정나면 수정하기
    public void updateUserPushState(boolean state, Long userId) {
        User user = UserServiceUtils.findUserById(userRepository, userId);
        RoomServiceUtils.findParticipatingRoom(user);
        Setting setting = user.getSetting();
        UserServiceUtils.validatePushNotificationStatus(setting.isPushNotification(), state);
        setting.setPushNotification(state);
    }

    public void updateUserTestScore(UpdateTestScoreRequestDto request, Long userId) {
        User user = UserServiceUtils.findUserById(userRepository, userId);
        RoomServiceUtils.findParticipatingRoom(user);
        Onboarding onboarding = user.getOnboarding();
        TestScore testScore = onboarding.getTestScore();
        testScore.updateScore(request.getLight(), request.getNoise(), request.getClean(), request.getSmell(), request.getIntroversion());
        Personality personality = UserServiceUtils.getPersonalityColorByTestScore(personalityRepository, testScore);
        onboarding.setPersonality(personality);
    }

    public void deleteUser(Long userId) {
        User user = UserServiceUtils.findUserById(userRepository, userId);
        List<Participate> participateList = user.getOnboarding().getParticipates();
        if (!participateList.isEmpty()) {
            throw new ForbiddenException(String.format("방 (%s) 의 참가자 는 탈퇴할 수 없습니다.", participateList.get(0).getRoom().getId()),
                    FORBIDDEN_USER_DELETE_ROOM_PARTICIPATE_EXCEPTION);
        }
        if (userRepository.deleteUserById(userId) != 1) {
            throw new NotFoundException(String.format("존재 하지 않은 유저 (%s) 는 탈퇴할 수 없습니다.", userId),
                    NOTFOUND_USER_EXCEPTION);
        }
    }
}
