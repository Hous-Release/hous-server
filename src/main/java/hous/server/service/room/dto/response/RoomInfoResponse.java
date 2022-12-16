package hous.server.service.room.dto.response;

import hous.server.domain.room.Room;
import lombok.*;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class RoomInfoResponse {

    private Long roomId;

    private String roomCode;

    public static RoomInfoResponse of(Room room) {
        return RoomInfoResponse.builder()
                .roomId(room.getId())
                .roomCode(room.getCode())
                .build();
    }
}
