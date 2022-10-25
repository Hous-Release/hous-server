package hous.server.service.user;

import hous.server.common.util.JwtUtils;
import hous.server.domain.badge.Badge;
import hous.server.domain.badge.BadgeInfo;
import hous.server.domain.badge.Represent;
import hous.server.domain.badge.repository.AcquireRepository;
import hous.server.domain.badge.repository.BadgeRepository;
import hous.server.domain.badge.repository.RepresentRepository;
import hous.server.domain.common.RedisKey;
import hous.server.domain.personality.Personality;
import hous.server.domain.personality.PersonalityColor;
import hous.server.domain.personality.repository.PersonalityRepository;
import hous.server.domain.room.Participate;
import hous.server.domain.room.Room;
import hous.server.domain.room.repository.RoomRepository;
import hous.server.domain.todo.Todo;
import hous.server.domain.todo.repository.DoneRepository;
import hous.server.domain.todo.repository.TakeRepository;
import hous.server.domain.todo.repository.TodoRepository;
import hous.server.domain.user.Onboarding;
import hous.server.domain.user.Setting;
import hous.server.domain.user.TestScore;
import hous.server.domain.user.User;
import hous.server.domain.user.repository.OnboardingRepository;
import hous.server.domain.user.repository.SettingRepository;
import hous.server.domain.user.repository.TestScoreRepository;
import hous.server.domain.user.repository.UserRepository;
import hous.server.service.badge.BadgeService;
import hous.server.service.badge.BadgeServiceUtils;
import hous.server.service.room.RoomServiceUtils;
import hous.server.service.todo.TodoServiceUtils;
import hous.server.service.user.dto.request.CreateUserRequestDto;
import hous.server.service.user.dto.request.UpdatePushSettingRequestDto;
import hous.server.service.user.dto.request.UpdateTestScoreRequestDto;
import hous.server.service.user.dto.request.UpdateUserInfoRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class UserService {

    private final RedisTemplate<String, Object> redisTemplate;

    private final UserRepository userRepository;
    private final OnboardingRepository onboardingRepository;
    private final SettingRepository settingRepository;
    private final TestScoreRepository testScoreRepository;
    private final PersonalityRepository personalityRepository;
    private final BadgeRepository badgeRepository;
    private final AcquireRepository acquireRepository;
    private final RepresentRepository representRepository;
    private final TakeRepository takeRepository;
    private final DoneRepository doneRepository;
    private final TodoRepository todoRepository;
    private final RoomRepository roomRepository;

    private final BadgeService badgeService;

    private final JwtUtils jwtProvider;

    public Long registerUser(CreateUserRequestDto request) {
        UserServiceUtils.validateNotExistsUser(userRepository, request.getSocialId(), request.getSocialType());
        User user = userRepository.save(User.newInstance(
                request.getSocialId(), request.getSocialType(),
                settingRepository.save(Setting.newInstance())));
        Onboarding onboarding = onboardingRepository.save(Onboarding.newInstance(
                user,
                personalityRepository.findPersonalityByColor(PersonalityColor.GRAY),
                request.getNickname(),
                request.getBirthday(),
                request.getIsPublic()));
        User conflictFcmTokenUser = userRepository.findUserByFcmToken(request.getFcmToken());
        if (conflictFcmTokenUser != null) {
            jwtProvider.expireRefreshToken(conflictFcmTokenUser.getId());
            conflictFcmTokenUser.resetFcmToken();
        }
        user.updateFcmToken(request.getFcmToken());
        user.setOnboarding(onboarding);
        return user.getId();
    }

    public void updateUserInfo(UpdateUserInfoRequestDto request, Long userId) {
        User user = UserServiceUtils.findUserById(userRepository, userId);
        RoomServiceUtils.findParticipatingRoom(user);
        Onboarding onboarding = user.getOnboarding();
        onboarding.updateUserInfo(request);
    }

    public void updateUserPushSetting(UpdatePushSettingRequestDto request, Long userId) {
        User user = UserServiceUtils.findUserById(userRepository, userId);
        Setting setting = user.getSetting();
        UserServiceUtils.validatePushSettingRequest(request, user);
        setting.updatePushSetting(request);
    }

    public void updateUserTestScore(UpdateTestScoreRequestDto request, Long userId) {
        User user = UserServiceUtils.findUserById(userRepository, userId);
        Room room = RoomServiceUtils.findParticipatingRoom(user);
        Onboarding me = user.getOnboarding();
        TestScore testScore = me.getTestScore();
        if (testScore == null) {
            testScore = testScoreRepository.save(TestScore.newInstance());
            me.setTestScore(testScore);
        }
        testScore.updateTestScore(request.getLight(), request.getNoise(), request.getClean(), request.getSmell(), request.getIntroversion());
        Personality personality = UserServiceUtils.getPersonalityColorByTestScore(personalityRepository, testScore);
        me.updatePersonality(personality);
        badgeService.acquireBadge(user, BadgeInfo.I_AM_SUCH_A_PERSON);
        if (!BadgeServiceUtils.hasBadge(badgeRepository, acquireRepository, BadgeInfo.I_DONT_EVEN_KNOW_ME, me)) {
            String personalityTestCountString = (String) redisTemplate.opsForValue().get(RedisKey.PERSONALITY_TEST_COUNT + userId);
            if (personalityTestCountString != null && Integer.parseInt(personalityTestCountString) >= 4) {
                badgeService.acquireBadge(user, BadgeInfo.I_DONT_EVEN_KNOW_ME);
                redisTemplate.delete(RedisKey.PERSONALITY_TEST_COUNT + user.getId());
            } else {
                if (personalityTestCountString == null) {
                    redisTemplate.opsForValue().set(RedisKey.PERSONALITY_TEST_COUNT + user.getId(), Integer.toString(1));
                } else {
                    redisTemplate.opsForValue().set(RedisKey.PERSONALITY_TEST_COUNT + user.getId(), Integer.toString(Integer.parseInt(personalityTestCountString) + 1));
                }
            }
        }
        List<Participate> participates = room.getParticipates();
        int testCompleteCnt = (int) participates.stream()
                .filter(participate -> participate.getOnboarding().getPersonality().getColor() != PersonalityColor.GRAY)
                .count();
        if (room.getParticipantsCnt() == testCompleteCnt) {
            participates.forEach(participate -> {
                Onboarding onboarding = participate.getOnboarding();
                badgeService.acquireBadge(onboarding.getUser(), BadgeInfo.OUR_HOUSE_HOMIES);
            });
        }
    }

    public void updateRepresentBadge(Long badgeId, Long userId) {
        User user = UserServiceUtils.findUserById(userRepository, userId);
        Onboarding me = user.getOnboarding();
        RoomServiceUtils.findParticipatingRoom(user);
        Badge badge = BadgeServiceUtils.findBadgeById(badgeRepository, badgeId);
        BadgeServiceUtils.validateExistsByOnboardingAndBadge(acquireRepository, me, badge);
        Represent represent = representRepository.save(Represent.newInstance(me, badge));
        me.updateRepresent(represent);
    }

    public void deleteUser(Long userId) {
        User user = UserServiceUtils.findUserById(userRepository, userId);
        Room room = RoomServiceUtils.findParticipatingRoom(user);
        Onboarding me = user.getOnboarding();

        List<Todo> todos = room.getTodos();
        List<Todo> myTodos = TodoServiceUtils.filterAllDaysUserTodos(todos, me);
        RoomServiceUtils.deleteMyTodosTakeMe(takeRepository, doneRepository, todoRepository, myTodos, me, room);

        userRepository.delete(user);
        if (room.getParticipates().isEmpty()) {
            roomRepository.delete(room);
        }
    }

    public void acquireFeedbackBadge(Long userId) {
        User user = UserServiceUtils.findUserById(userRepository, userId);
        RoomServiceUtils.findParticipatingRoom(user);
        badgeService.acquireBadge(user, BadgeInfo.FEEDBACK_ONE_STEP);
    }

}
