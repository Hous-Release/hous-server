package hous.api.service.user;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hous.api.service.badge.BadgeService;
import hous.api.service.badge.BadgeServiceUtils;
import hous.api.service.room.RoomServiceUtils;
import hous.api.service.todo.TodoServiceUtils;
import hous.api.service.user.dto.request.CreateUserRequestDto;
import hous.api.service.user.dto.request.DeleteUserRequestDto;
import hous.api.service.user.dto.request.UpdatePushSettingRequestDto;
import hous.api.service.user.dto.request.UpdateTestScoreRequestDto;
import hous.api.service.user.dto.request.UpdateUserInfoRequestDto;
import hous.api.service.user.dto.response.UpdatePersonalityColorResponse;
import hous.common.util.JwtUtils;
import hous.core.domain.badge.Badge;
import hous.core.domain.badge.BadgeCounter;
import hous.core.domain.badge.BadgeCounterType;
import hous.core.domain.badge.BadgeInfo;
import hous.core.domain.badge.Represent;
import hous.core.domain.badge.mongo.BadgeCounterRepository;
import hous.core.domain.badge.mysql.AcquireRepository;
import hous.core.domain.badge.mysql.BadgeRepository;
import hous.core.domain.badge.mysql.RepresentRepository;
import hous.core.domain.feedback.Feedback;
import hous.core.domain.feedback.mysql.FeedbackRepository;
import hous.core.domain.personality.Personality;
import hous.core.domain.personality.PersonalityColor;
import hous.core.domain.personality.mysql.PersonalityRepository;
import hous.core.domain.room.Participate;
import hous.core.domain.room.Room;
import hous.core.domain.room.mysql.ParticipateRepository;
import hous.core.domain.room.mysql.RoomRepository;
import hous.core.domain.todo.Todo;
import hous.core.domain.todo.mysql.DoneRepository;
import hous.core.domain.todo.mysql.TakeRepository;
import hous.core.domain.todo.mysql.TodoRepository;
import hous.core.domain.user.Onboarding;
import hous.core.domain.user.Setting;
import hous.core.domain.user.TestScore;
import hous.core.domain.user.User;
import hous.core.domain.user.mysql.OnboardingRepository;
import hous.core.domain.user.mysql.SettingRepository;
import hous.core.domain.user.mysql.TestScoreRepository;
import hous.core.domain.user.mysql.UserRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional
public class UserService {

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
	private final ParticipateRepository participateRepository;
	private final FeedbackRepository feedbackRepository;
	private final BadgeCounterRepository badgeCounterRepository;

	private final BadgeService badgeService;
	private final JwtUtils jwtUtils;

	public Long registerUser(CreateUserRequestDto request) {
		UserServiceUtils.validateNotExistsUser(userRepository, request.getSocialId(), request.getSocialType());
		UserServiceUtils.validateBirthdayAndIsPublic(request.getBirthday(), request.isPublic());
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
			jwtUtils.expireRefreshToken(conflictFcmTokenUser.getId());
			conflictFcmTokenUser.resetFcmToken();
		}
		user.updateFcmToken(request.getFcmToken());
		user.setOnboarding(onboarding);
		return user.getId();
	}

	public void updateUserInfo(UpdateUserInfoRequestDto request, Long userId) {
		UserServiceUtils.validateBirthdayAndIsPublic(request.getBirthday(), request.isPublic());
		User user = UserServiceUtils.findUserById(userRepository, userId);
		RoomServiceUtils.findParticipatingRoom(user);
		Onboarding onboarding = user.getOnboarding();
		onboarding.updateUserInfo(request.getNickname(), request.isPublic(), request.getBirthday(), request.getMbti(),
			request.getJob(), request.getIntroduction());
	}

	public void updateUserPushSetting(UpdatePushSettingRequestDto request, Long userId) {
		User user = UserServiceUtils.findUserById(userRepository, userId);
		Setting setting = user.getSetting();
		UserServiceUtils.validatePushSettingRequest(request, user);
		setting.updatePushSetting(request.isPushNotification(), request.getRulesPushStatus(),
			request.getNewTodoPushStatus(), request.getTodayTodoPushStatus(), request.getRemindTodoPushStatus(),
			request.getBadgePushStatus());
	}

	public UpdatePersonalityColorResponse updateUserTestScore(UpdateTestScoreRequestDto request, Long userId) {
		User user = UserServiceUtils.findUserById(userRepository, userId);
		Room room = RoomServiceUtils.findParticipatingRoom(user);
		Onboarding me = user.getOnboarding();
		TestScore testScore = me.getTestScore();
		if (testScore == null) {
			testScore = testScoreRepository.save(TestScore.newInstance());
			me.setTestScore(testScore);
		}
		testScore.updateTestScore(request.getLight(), request.getNoise(), request.getClean(), request.getSmell(),
			request.getIntroversion());
		Personality personality = UserServiceUtils.getPersonalityColorByTestScore(personalityRepository, testScore);
		me.updatePersonality(personality);

		badgeService.acquireBadge(user, BadgeInfo.I_AM_SUCH_A_PERSON);
		if (!BadgeServiceUtils.hasBadge(badgeRepository, acquireRepository, BadgeInfo.I_DONT_EVEN_KNOW_ME, me)) {
			BadgeCounter testScoreComplete = badgeCounterRepository.findByUserIdAndCountType(userId,
				BadgeCounterType.TEST_SCORE_COMPLETE);
			if (testScoreComplete != null && testScoreComplete.getCount() >= 4) {
				badgeService.acquireBadge(user, BadgeInfo.I_DONT_EVEN_KNOW_ME);
				badgeCounterRepository.deleteBadgeCounterByUserIdAndCountType(userId,
					BadgeCounterType.TEST_SCORE_COMPLETE);
			} else {
				if (testScoreComplete == null) {
					badgeCounterRepository.save(
						BadgeCounter.newInstance(userId, BadgeCounterType.TEST_SCORE_COMPLETE, 1));
				} else {
					testScoreComplete.updateCount(testScoreComplete.getCount() + 1);
					badgeCounterRepository.save(testScoreComplete);
				}
			}
		}

		List<Participate> participates = room.getParticipates();
		int testCompleteCnt = (int)participates.stream()
			.filter(participate -> participate.getOnboarding().getPersonality().getColor() != PersonalityColor.GRAY)
			.count();
		if (room.getParticipantsCnt() == testCompleteCnt) {
			participates.forEach(participate -> {
				Onboarding onboarding = participate.getOnboarding();
				badgeService.acquireBadge(onboarding.getUser(), BadgeInfo.OUR_HOUSE_HOMIES);
			});
		}
		return UpdatePersonalityColorResponse.of(personality);
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

	public void deleteUserDeprecated(DeleteUserRequestDto request, Long userId) {
		User user = UserServiceUtils.findUserById(userRepository, userId);
		Onboarding me = user.getOnboarding();
		List<Participate> participates = me.getParticipates();

		if (!participates.isEmpty()) {
			Participate participate = participates.get(0);
			Room room = participate.getRoom();
			List<Todo> todos = room.getTodos();
			List<Todo> myTodos = TodoServiceUtils.filterAllDaysUserTodos(todos, me);
			RoomServiceUtils.deleteMyTodosTakeMe(takeRepository, doneRepository, todoRepository, myTodos, me, room);
			RoomServiceUtils.deleteParticipateUser(participateRepository, roomRepository, me, room, participate);
		}

		if (UserServiceUtils.isNewFeedback(request.getFeedbackType(), request.getComment())) {
			feedbackRepository.save(Feedback.newInstance(request.getFeedbackType(), request.getComment()));
		}
		userRepository.delete(user);
	}

	public void acquireFeedbackBadge(Long userId) {
		User user = UserServiceUtils.findUserById(userRepository, userId);
		RoomServiceUtils.findParticipatingRoom(user);
		badgeService.acquireBadge(user, BadgeInfo.FEEDBACK_ONE_STEP);
	}

	public void deleteUser(Long userId) {
		User user = UserServiceUtils.findUserById(userRepository, userId);
		Onboarding me = user.getOnboarding();
		List<Participate> participates = me.getParticipates();

		if (!participates.isEmpty()) {
			Participate participate = participates.get(0);
			Room room = participate.getRoom();
			List<Todo> todos = room.getTodos();
			List<Todo> myTodos = TodoServiceUtils.filterAllDaysUserTodos(todos, me);
			RoomServiceUtils.deleteMyTodosTakeMe(takeRepository, doneRepository, todoRepository, myTodos, me, room);
			RoomServiceUtils.deleteParticipateUser(participateRepository, roomRepository, me, room, participate);
		}

		userRepository.delete(user);
	}
}
