package hous.server.service.room;

import hous.server.common.exception.ConflictException;
import hous.server.common.exception.NotFoundException;
import hous.server.domain.room.Room;
import hous.server.domain.room.repository.ParticipateRepository;
import hous.server.domain.room.repository.RoomRepository;
import hous.server.domain.user.Onboarding;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Random;

import static hous.server.common.exception.ErrorCode.CONFLICT_JOINED_ROOM_EXCEPTION;
import static hous.server.common.exception.ErrorCode.NOT_FOUND_ROOM_EXCEPTION;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RoomServiceUtils {

    public static void validateNotExistsParticipate(ParticipateRepository participateRepository, Onboarding onboarding) {
        if (participateRepository.existsByOnboarding(onboarding)) {
            throw new ConflictException(String.format("이미 참가중인 방이 있는 유저 (%s) 입니다.", onboarding.getId()), CONFLICT_JOINED_ROOM_EXCEPTION);
        }
    }

    public static Room findRoomById(RoomRepository roomRepository, Long roomId) {
        Room room = roomRepository.findRoomById(roomId);
        if (room == null) {
            throw new NotFoundException(String.format("존재하지 않는 방 (%s) 입니다", roomId), NOT_FOUND_ROOM_EXCEPTION);
        }
        return room;
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
