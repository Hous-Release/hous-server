package hous.api.service.room;

import hous.api.service.badge.BadgeService;
import hous.api.service.room.dto.request.SetRoomNameRequestDto;
import hous.api.service.room.dto.response.RoomInfoResponse;
import hous.api.service.todo.TodoServiceUtils;
import hous.api.service.user.UserServiceUtils;
import hous.core.domain.badge.BadgeInfo;
import hous.core.domain.badge.mysql.RepresentRepository;
import hous.core.domain.notification.mongo.NotificationRepository;
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
import hous.core.domain.user.User;
import hous.core.domain.user.mysql.TestScoreRepository;
import hous.core.domain.user.mysql.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@RequiredArgsConstructor
@Service
@Transactional
public class RoomService {

    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final ParticipateRepository participateRepository;
    private final TodoRepository todoRepository;
    private final TakeRepository takeRepository;
    private final DoneRepository doneRepository;
    private final RepresentRepository representRepository;
    private final TestScoreRepository testScoreRepository;
    private final PersonalityRepository personalityRepository;
    private final NotificationRepository notificationRepository;

    private final BadgeService badgeService;

    public RoomInfoResponse createRoom(SetRoomNameRequestDto request, Long userId) {
        User user = UserServiceUtils.findUserById(userRepository, userId);
        Onboarding me = user.getOnboarding();
        RoomServiceUtils.validateNotExistsParticipate(participateRepository, me);
        Room room = roomRepository.save(Room.newInstance(me, request.getName(), createUniqueRoomCode()));
        Participate participate = participateRepository.save(Participate.newInstance(me, room));
        me.addParticipate(participate);
        room.addParticipate(participate);
        badgeService.acquireBadge(user, BadgeInfo.POUNDING_HOUSE);
        return RoomInfoResponse.of(room);
    }

    public RoomInfoResponse joinRoom(Long roomId, Long userId) {
        User user = UserServiceUtils.findUserById(userRepository, userId);
        Onboarding me = user.getOnboarding();
        RoomServiceUtils.validateNotExistsParticipate(participateRepository, me);
        Room room = RoomServiceUtils.findRoomById(roomRepository, roomId);
        RoomServiceUtils.validateParticipateCounts(room);
        Participate participate = participateRepository.save(Participate.newInstance(me, room));
        me.addParticipate(participate);
        room.addParticipate(participate);
        badgeService.acquireBadge(user, BadgeInfo.POUNDING_HOUSE);
        return RoomInfoResponse.of(room);
    }

    public void updateRoomName(SetRoomNameRequestDto request, Long userId) {
        User user = UserServiceUtils.findUserById(userRepository, userId);
        Room room = RoomServiceUtils.findParticipatingRoom(user);
        room.updateRoomName(request.getName());
    }

    public void leaveRoom(Long userId) {
        User user = UserServiceUtils.findUserById(userRepository, userId);
        Room room = RoomServiceUtils.findParticipatingRoom(user);
        Onboarding me = user.getOnboarding();
        Participate participate = me.getParticipates().get(0);

        List<Todo> todos = room.getTodos();
        List<Todo> myTodos = TodoServiceUtils.filterAllDaysUserTodos(todos, me);

        RoomServiceUtils.deleteMyTodosTakeMe(takeRepository, doneRepository, todoRepository, myTodos, me, room);
        RoomServiceUtils.deleteParticipateUser(participateRepository, roomRepository, me, room, participate);

        // 내 배지, 알림 목록, 프로필 작성 내역, 테스트 결과 초기화
        if (me.getRepresent() != null) {
            representRepository.delete(me.getRepresent());
        }
        if (me.getTestScore() != null) {
            testScoreRepository.delete(me.getTestScore());
            me.updatePersonality(personalityRepository.findPersonalityByColor(PersonalityColor.GRAY));
        }
        me.resetUserInfo();
        me.resetBadge();
        notificationRepository.deleteAllByOnboarding(me);
    }

    public boolean existsParticipatingRoomByUserId(Long userId) {
        User user = UserServiceUtils.findUserById(userRepository, userId);
        List<Participate> participates = user.getOnboarding().getParticipates();
        return !participates.isEmpty();
    }

    private String createUniqueRoomCode() {
        String code;
        do {
            Random random = new Random();
            code = random.ints(48, 91)
                    .filter(i -> (i <= 57 || i >= 65) && (i <= 90))
                    .limit(8)
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString();
        } while (isNotUniqueRoomCode(code));
        return code;
    }

    private boolean isNotUniqueRoomCode(String code) {
        return roomRepository.existsByRoomCode(code);
    }
}
