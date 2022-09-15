package hous.server.service.room;

import hous.server.domain.badge.repository.AcquireRepository;
import hous.server.domain.badge.repository.RepresentRepository;
import hous.server.domain.room.Participate;
import hous.server.domain.room.Room;
import hous.server.domain.room.repository.ParticipateRepository;
import hous.server.domain.room.repository.RoomRepository;
import hous.server.domain.todo.Done;
import hous.server.domain.todo.Take;
import hous.server.domain.todo.Todo;
import hous.server.domain.todo.repository.DoneRepository;
import hous.server.domain.todo.repository.TakeRepository;
import hous.server.domain.todo.repository.TodoRepository;
import hous.server.domain.user.Onboarding;
import hous.server.domain.user.User;
import hous.server.domain.user.repository.UserRepository;
import hous.server.service.room.dto.request.SetRoomNameRequestDto;
import hous.server.service.room.dto.response.RoomInfoResponse;
import hous.server.service.todo.TodoServiceUtils;
import hous.server.service.user.UserServiceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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

    public RoomInfoResponse createRoom(SetRoomNameRequestDto request, Long userId) {
        User user = UserServiceUtils.findUserById(userRepository, userId);
        Onboarding onboarding = user.getOnboarding();
        RoomServiceUtils.validateNotExistsParticipate(participateRepository, onboarding);
        Room room = roomRepository.save(Room.newInstance(onboarding, request.getName(), RoomServiceUtils.createUniqueRoomCode(roomRepository)));
        Participate participate = participateRepository.save(Participate.newInstance(onboarding, room));
        onboarding.addParticipate(participate);
        room.addParticipate(participate);
        return RoomInfoResponse.of(room);
    }

    public RoomInfoResponse joinRoom(Long roomId, Long userId) {
        User user = UserServiceUtils.findUserById(userRepository, userId);
        Onboarding onboarding = user.getOnboarding();
        RoomServiceUtils.validateNotExistsParticipate(participateRepository, onboarding);
        Room room = RoomServiceUtils.findRoomById(roomRepository, roomId);
        RoomServiceUtils.validateParticipateCounts(participateRepository, room);
        Participate participate = participateRepository.save(Participate.newInstance(onboarding, room));
        onboarding.addParticipate(participate);
        room.addParticipate(participate);
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
        myTodos.forEach(todo -> {
            // todo 담당자가 여러명이면 나의 담당 해제
            if (todo.getTakes().size() > 1) {
                Optional<Take> myTake = todo.getTakes().stream()
                        .filter(take -> take.getOnboarding().getId().equals(me.getId()))
                        .findFirst();
                if (myTake.isPresent()) {
                    List<Done> myDones = TodoServiceUtils.filterAllDaysMyDones(me, todo.getDones());
                    takeRepository.delete(myTake.get());
                    myDones.forEach(todo::deleteDone);
                    doneRepository.deleteAll(myDones);
                    todo.deleteTake(myTake.get());
                }
            }
            // todo 담당자가 나뿐이면 todo 삭제
            else {
                Todo myTodo = myTodos.get(0);
                room.deleteTodo(myTodo);
                todoRepository.delete(myTodo);
            }
        });

        // 방의 참가자가 여러명이면 나만 방을 나감
        List<Participate> participates = room.getParticipates();
        if (participates.size() > 1) {
            room.deleteParticipate(participates.get(0));
            me.deleteParticipate(participates.get(0));
            participateRepository.delete(participates.get(0));
        }
        // 방의 참가자가 나 혼자면 방을 나가고 삭제
        else {
            me.deleteParticipate(participates.get(0));
            roomRepository.delete(room);
        }

        // 내 뱃지, 프로필 작성 내역, 테스트 결과 초기화
        if (me.getRepresent() != null) {
            representRepository.delete(me.getRepresent());
        }
        acquireRepository.deleteAll(me.getAcquires());
        me.resetUserInfo();
        me.resetBadge();
        me.resetTestScore(me.getTestScore());
    }
}
