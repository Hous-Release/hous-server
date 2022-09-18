package hous.server.service.user;

import hous.server.common.exception.ConflictException;
import hous.server.common.exception.NotFoundException;
import hous.server.common.exception.ValidationException;
import hous.server.domain.personality.Personality;
import hous.server.domain.personality.PersonalityColor;
import hous.server.domain.personality.repository.PersonalityRepository;
import hous.server.domain.user.TestScore;
import hous.server.domain.user.User;
import hous.server.domain.user.UserSocialType;
import hous.server.domain.user.repository.UserRepository;
import hous.server.service.user.dto.request.UpdatePushSettingRequestDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static hous.server.common.exception.ErrorCode.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserServiceUtils {

    static void validateNotExistsUser(UserRepository userRepository, String socialId, UserSocialType socialType) {
        if (userRepository.existsBySocialIdAndSocialType(socialId, socialType)) {
            throw new ConflictException(String.format("이미 존재하는 유저 (%s - %s) 입니다", socialId, socialType), CONFLICT_USER_EXCEPTION);
        }
    }

    public static User findUserBySocialIdAndSocialType(UserRepository userRepository, String socialId, UserSocialType socialType) {
        return userRepository.findUserBySocialIdAndSocialType(socialId, socialType);
    }

    public static User findUserById(UserRepository userRepository, Long userId) {
        User user = userRepository.findUserById(userId);
        if (user == null) {
            throw new NotFoundException(String.format("존재하지 않는 유저 (%s) 입니다", userId), NOT_FOUND_USER_EXCEPTION);
        }
        return user;
    }

    public static void validatePushSettingRequestStatus(UpdatePushSettingRequestDto request, User user) {
        if (request.isPushNotification() == user.getSetting().isPushNotification() &&
                request.getRulesPushStatus() == user.getSetting().getRulesPushStatus() &&
                request.getNewTodoPushStatus() == user.getSetting().getNewTodoPushStatus() &&
                request.getTodayTodoPushStatus() == user.getSetting().getTodayTodoPushStatus() &&
                request.getRemindTodoPushStatus() == user.getSetting().getRemindTodoPushStatus() &&
                request.getBadgePushStatus() == user.getSetting().getBadgePushStatus()) {
            throw new ValidationException(String.format("(%s) 유저의 푸시 알림 상태 중복입니다.\n%s", user.getId(), request), VALIDATION_STATUS_EXCEPTION);
        }
    }

    public static void validatePersonalityColor(PersonalityColor color) {
        if (color == PersonalityColor.GRAY) {
            throw new NotFoundException("GRAY 에 대한 성향 정보는 존재하지 않습니다.", NOT_FOUND_PERSONALITY_COLOR_EXCEPTION);
        }
    }

    public static Personality getPersonalityColorByTestScore(PersonalityRepository personalityRepository, TestScore testScore) {
        int totalTestScore = testScore.getTotalTestScore();
        if (totalTestScore >= 15 && totalTestScore <= 20) {
            return personalityRepository.findPersonalityByColor(PersonalityColor.YELLOW);
        } else if (totalTestScore >= 21 && totalTestScore <= 26) {
            return personalityRepository.findPersonalityByColor(PersonalityColor.RED);
        } else if (totalTestScore >= 27 && totalTestScore <= 33) {
            return personalityRepository.findPersonalityByColor(PersonalityColor.BLUE);
        } else if (totalTestScore >= 34 && totalTestScore <= 39) {
            return personalityRepository.findPersonalityByColor(PersonalityColor.PURPLE);
        } else if (totalTestScore >= 40 && totalTestScore <= 45) {
            return personalityRepository.findPersonalityByColor(PersonalityColor.GREEN);
        }
        return null;
    }

}
