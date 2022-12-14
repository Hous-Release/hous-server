package hous.server.service.user;

import hous.server.common.exception.ConflictException;
import hous.server.common.exception.NotFoundException;
import hous.server.common.exception.ValidationException;
import hous.server.domain.feedback.FeedbackType;
import hous.server.domain.personality.Personality;
import hous.server.domain.personality.PersonalityColor;
import hous.server.domain.personality.repository.PersonalityRepository;
import hous.server.domain.user.Onboarding;
import hous.server.domain.user.TestScore;
import hous.server.domain.user.User;
import hous.server.domain.user.UserSocialType;
import hous.server.domain.user.repository.OnboardingRepository;
import hous.server.domain.user.repository.UserRepository;
import hous.server.service.user.dto.request.UpdatePushSettingRequestDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static hous.server.common.exception.ErrorCode.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserServiceUtils {

    static void validateNotExistsUser(UserRepository userRepository, String socialId, UserSocialType socialType) {
        if (userRepository.existsBySocialIdAndSocialType(socialId, socialType)) {
            throw new ConflictException(String.format("이미 존재하는 유저 (%s - %s) 입니다", socialId, socialType), CONFLICT_USER_EXCEPTION);
        }
    }

    public static User findUserBySocialIdAndSocialType(UserRepository userRepository, String socialId, UserSocialType socialType) {
        User user = userRepository.findUserBySocialIdAndSocialType(socialId, socialType);
        if (user == null) {
            throw new NotFoundException(String.format("존재하지 않는 유저 (%s) (%s) 입니다", socialType, socialId), NOT_FOUND_USER_EXCEPTION);
        }
        return user;
    }

    public static User findUserById(UserRepository userRepository, Long userId) {
        User user = userRepository.findUserById(userId);
        if (user == null) {
            throw new NotFoundException(String.format("존재하지 않는 유저 (%s) 입니다", userId), NOT_FOUND_USER_EXCEPTION);
        }
        return user;
    }

    public static Onboarding findOnboardingById(OnboardingRepository onboardingRepository, Long onboardingId) {
        Onboarding onboarding = onboardingRepository.findOnboardingById(onboardingId);
        if (onboarding == null) {
            throw new NotFoundException(String.format("존재하지 않는 온보딩 정보 (%s) 입니다", onboardingId), NOT_FOUND_ONBOARDING_EXCEPTION);
        }
        return onboarding;
    }

    public static void validatePushSettingRequest(UpdatePushSettingRequestDto request, User user) {
        int notNullStatusCnt = 0;
        if (request.isPushNotification() != null) {
            if (request.isPushNotification() == user.getSetting().isPushNotification()) {
                throw new ValidationException(String.format("(%s) 유저의 알림 상태 중복입니다.", user.getId()), VALIDATION_STATUS_EXCEPTION);
            }
            notNullStatusCnt++;
        }
        if (request.getRulesPushStatus() != null) {
            if (request.getRulesPushStatus() == user.getSetting().getRulesPushStatus()) {
                throw new ValidationException(String.format("(%s) 유저의 알림 상태 중복입니다.", user.getId()), VALIDATION_STATUS_EXCEPTION);
            }
            notNullStatusCnt++;
        }
        if (request.getNewTodoPushStatus() != null) {
            if (request.getNewTodoPushStatus() == user.getSetting().getNewTodoPushStatus()) {
                throw new ValidationException(String.format("(%s) 유저의 알림 상태 중복입니다.", user.getId()), VALIDATION_STATUS_EXCEPTION);
            }
            notNullStatusCnt++;
        }
        if (request.getTodayTodoPushStatus() != null) {
            if (request.getTodayTodoPushStatus() == user.getSetting().getTodayTodoPushStatus()) {
                throw new ValidationException(String.format("(%s) 유저의 알림 상태 중복입니다.", user.getId()), VALIDATION_STATUS_EXCEPTION);
            }
            notNullStatusCnt++;
        }
        if (request.getRemindTodoPushStatus() != null) {
            if (request.getRemindTodoPushStatus() == user.getSetting().getRemindTodoPushStatus()) {
                throw new ValidationException(String.format("(%s) 유저의 알림 상태 중복입니다.", user.getId()), VALIDATION_STATUS_EXCEPTION);
            }
            notNullStatusCnt++;
        }
        if (request.getBadgePushStatus() != null) {
            if (request.getBadgePushStatus() == user.getSetting().getBadgePushStatus()) {
                throw new ValidationException(String.format("(%s) 유저의 알림 상태 중복입니다.", user.getId()), VALIDATION_STATUS_EXCEPTION);
            }
            notNullStatusCnt++;
        }
        if (notNullStatusCnt != 1) {
            throw new ValidationException(String.format("(%s) 유저의 잘못된 요청 (%s) 입니다.", user.getId(), request), VALIDATION_EXCEPTION);
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
        return personalityRepository.findPersonalityByColor(PersonalityColor.GRAY);
    }

    public static List<Onboarding> toMeFirstList(List<Onboarding> onboardings, Onboarding me) {
        List<Onboarding> result = new ArrayList<>();
        List<Onboarding> justMeList = onboardings.stream()
                .filter(onboarding -> onboarding.getId().equals(me.getId()))
                .collect(Collectors.toList());
        List<Onboarding> exceptMeList = onboardings.stream()
                .filter(onboarding -> !onboarding.getId().equals(me.getId()))
                .collect(Collectors.toList());
        result.addAll(justMeList);
        result.addAll(exceptMeList);
        return result;
    }

    public static boolean isNewFeedback(FeedbackType feedbackType, String comment) {
        return !(feedbackType.equals(FeedbackType.NO) && comment.isBlank());
    }

    public static void validateBirthdayAndIsPublic(String birthday, Boolean isPublic) {
        if (birthday.equals("") && isPublic) {
            throw new ValidationException(String.format("생년월일 (\"\") 을 지정하지 않은 경우, 공개 여부 (%s) 를 지정할 수 없습니다.", birthday, isPublic), VALIDATION_BIRTHDAY_EXCEPTION);
        }
    }
}
