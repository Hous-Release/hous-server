package hous.server.service.room;

import hous.server.common.exception.ConflictException;
import hous.server.common.exception.ForbiddenException;
import hous.server.common.exception.NotFoundException;
import hous.server.domain.room.Participate;
import hous.server.domain.room.Room;
import hous.server.domain.room.repository.ParticipateRepository;
import hous.server.domain.room.repository.RoomRepository;
import hous.server.domain.user.Onboarding;
import hous.server.domain.user.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.Random;

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

    private static boolean isNotUniqueRoomCode(RoomRepository roomRepository, String code) {
        return roomRepository.existsByRoomCode(code);
    }

    static String createUniqueRoomCode(RoomRepository roomRepository) {
        String code;
        do {
            Random random = new Random();
            code = random.ints(48, 91)
                    .filter(i -> (i <= 57 || i >= 65) && (i <= 90))
                    .limit(8)
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString();
        } while (isNotUniqueRoomCode(roomRepository, code));
        return code;
    }
}
