package hous.api.service.room;

import static hous.common.exception.ErrorCode.*;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import hous.api.service.todo.TodoServiceUtils;
import hous.common.constant.Constraint;
import hous.common.exception.ConflictException;
import hous.common.exception.ForbiddenException;
import hous.common.exception.NotFoundException;
import hous.core.domain.room.Participate;
import hous.core.domain.room.Room;
import hous.core.domain.room.mysql.ParticipateRepository;
import hous.core.domain.room.mysql.RoomRepository;
import hous.core.domain.todo.Done;
import hous.core.domain.todo.Take;
import hous.core.domain.todo.Todo;
import hous.core.domain.todo.mysql.DoneRepository;
import hous.core.domain.todo.mysql.TakeRepository;
import hous.core.domain.todo.mysql.TodoRepository;
import hous.core.domain.user.Onboarding;
import hous.core.domain.user.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RoomServiceUtils {

	public static void validateNotExistsParticipate(ParticipateRepository participateRepository,
		Onboarding onboarding) {
		if (participateRepository.existsByOnboarding(onboarding)) {
			throw new ConflictException(String.format("이미 참가중인 방이 있는 유저 (%s) 입니다.", onboarding.getId()),
				CONFLICT_JOINED_ROOM_EXCEPTION);
		}
	}

	public static void validateParticipateCounts(Room room) {
		if (room.getParticipantsCnt() >= Constraint.ROOM_PARTICIPATE_MAX) {
			throw new ForbiddenException(String.format("방 (%s) 의 참가자 는 16명을 초과할 수 없습니다.", room.getId()),
				FORBIDDEN_PARTICIPATE_COUNT_EXCEPTION);
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
			throw new NotFoundException(String.format("유저 (%s) 는 참가중인 방이 존재하지 않습니다.", user.getId()),
				NOT_FOUND_PARTICIPATE_EXCEPTION);
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

	public static void deleteMyTodosTakeMe(TakeRepository takeRepository, DoneRepository doneRepository,
		TodoRepository todoRepository,
		List<Todo> myTodos, Onboarding me, Room room) {
		myTodos.forEach(todo -> {
			if (todo.getTakes().size() == 1) {
				room.deleteTodo(todo);
				todoRepository.delete(todo);
				return;
			}
			// todo 담당자가 여러명이면 나의 담당 해제
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
		});
	}

	public static void deleteParticipateUser(ParticipateRepository participateRepository, RoomRepository roomRepository,
		Onboarding me, Room room, Participate participate) {
		List<Participate> participates = room.getParticipates();
		if (participates.size() == 1) {
			me.deleteParticipate(participate);
			roomRepository.delete(room);
			return;
		}
		room.deleteParticipate(participate);
		me.deleteParticipate(participate);
		participateRepository.delete(participate);
		if (me.getId().equals(room.getOwner().getId())) {
			Optional<Participate> nextOwnerParticipate = room.getParticipates().stream()
				.min(Comparator.comparing(Participate::getCreatedAt));
			nextOwnerParticipate.ifPresent(nextOwner -> room.updateOwner(nextOwner.getOnboarding()));
		}
	}
}
