package hous.server.service.room;

import hous.server.common.exception.ConflictException;
import hous.server.common.exception.ForbiddenException;
import hous.server.common.exception.NotFoundException;
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
import hous.server.service.todo.TodoServiceUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static hous.server.common.exception.ErrorCode.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RoomServiceUtils {

    public static void validateNotExistsParticipate(ParticipateRepository participateRepository, Onboarding onboarding) {
        if (participateRepository.existsByOnboarding(onboarding)) {
            throw new ConflictException(String.format("이미 참가중인 방이 있는 유저 (%s) 입니다.", onboarding.getId()), CONFLICT_JOINED_ROOM_EXCEPTION);
        }
    }

    public static void validateParticipateCounts(ParticipateRepository participateRepository, Room room) {
        if (participateRepository.findCountsByRoom(room) >= 16) {
            throw new ForbiddenException(String.format("방 (%s) 의 참가자 는 16명을 초과할 수 없습니다.", room.getId()), FORBIDDEN_PARTICIPATE_COUNT_EXCEPTION);
        }
    }

    public static Room findRoomById(RoomRepository roomRepository, Long roomId) {
        Room room = roomRepository.findRoomById(roomId);
        if (room == null) {
            throw new NotFoundException(String.format("존재하지 않는 방 (%s) 입니다", roomId), NOT_FOUND_ROOM_EXCEPTION);
        }
        return room;
    }

    public static Room findParticipatingRoom(User user) {
        List<Participate> participates = user.getOnboarding().getParticipates();
        if (participates.isEmpty()) {
            throw new NotFoundException(String.format("유저 (%s) 는 참가중인 방이 존재하지 않습니다.", user.getId()), NOT_FOUND_PARTICIPATE_EXCEPTION);
        }
        return participates.get(0).getRoom();
    }

    public static void checkParticipatingRoom(Room userRoom, Room homieRoom) {
        if (!Objects.equals(userRoom.getId(), homieRoom.getId())) {
            throw new ForbiddenException(String.format("같은 방에 참가하고 있지 않습니다. (요청 사용자 방 id: %s, 호미 방 id: %s)",
                    userRoom.getId(), homieRoom.getId()), FORBIDDEN_ROOM_PARTICIPATE_EXCEPTION);
        }
    }

    public static List<User> findParticipatingUsersExceptMe(Room room, User me) {
        return room.getParticipates().stream()
                .filter(participate -> !participate.getOnboarding().getId().equals(me.getOnboarding().getId()))
                .map(participate -> participate.getOnboarding().getUser())
                .collect(Collectors.toList());
    }

    public static void deleteMyTodosTakeMe(TakeRepository takeRepository, DoneRepository doneRepository, TodoRepository todoRepository,
                                           List<Todo> myTodos, Onboarding me, Room room) {
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
    }
}
