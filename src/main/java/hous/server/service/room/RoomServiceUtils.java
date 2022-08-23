package hous.server.service.room;

import hous.server.common.exception.ConflictException;
import hous.server.common.exception.ErrorCode;
import hous.server.domain.room.repository.ParticipateRepository;
import hous.server.domain.room.repository.RoomRepository;
import hous.server.domain.user.Onboarding;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Random;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RoomServiceUtils {

    public static void validateNotExistsParticipate(ParticipateRepository participateRepository, Onboarding onboarding) {
        if (participateRepository.existsByOnboarding(onboarding)) {
            throw new ConflictException(String.format("이미 참가중인 방이 있는 유저 (%s) 입니다.", onboarding.getId()), ErrorCode.CONFLICT_JOINED_ROOM_EXCEPTION);
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
