package hous.server.service.room;

import hous.server.domain.badge.BadgeInfo;
import hous.server.domain.badge.repository.AcquireRepository;
import hous.server.domain.badge.repository.RepresentRepository;
import hous.server.domain.notification.repository.NotificationRepository;
import hous.server.domain.room.Participate;
import hous.server.domain.room.Room;
import hous.server.domain.room.repository.ParticipateRepository;
import hous.server.domain.room.repository.RoomRepository;
import hous.server.domain.todo.Todo;
import hous.server.domain.todo.repository.DoneRepository;
import hous.server.domain.todo.repository.TakeRepository;
import hous.server.domain.todo.repository.TodoRepository;
import hous.server.domain.user.Onboarding;
import hous.server.domain.user.User;
import hous.server.domain.user.repository.TestScoreRepository;
import hous.server.domain.user.repository.UserRepository;
import hous.server.service.badge.BadgeService;
import hous.server.service.room.dto.request.SetRoomNameRequestDto;
import hous.server.service.room.dto.response.RoomInfoResponse;
import hous.server.service.todo.TodoServiceUtils;
import hous.server.service.user.UserServiceUtils;
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
    private final AcquireRepository acquireRepository;
    private final RepresentRepository representRepository;
    private final TestScoreRepository testScoreRepository;
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

        List<Todo> todos = room.getTodos();
        List<Todo> myTodos = TodoServiceUtils.filterAllDaysUserTodos(todos, me);

        RoomServiceUtils.deleteMyTodosTakeMe(takeRepository, doneRepository, todoRepository, myTodos, me, room);
        RoomServiceUtils.deleteParticipateUser(participateRepository, roomRepository, me, room);
        
        // 내 배지, 알림 목록, 프로필 작성 내역, 테스트 결과 초기화
        if (me.getRepresent() != null) {
            representRepository.delete(me.getRepresent());
        }
        acquireRepository.deleteAll(me.getAcquires());
        notificationRepository.deleteAll(me.getNotifications());
        testScoreRepository.delete(me.getTestScore());
        me.resetUserInfo();
        me.resetBadge();
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
