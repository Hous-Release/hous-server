package hous.server.service.room.dto.response;

import hous.server.domain.room.Room;
import lombok.*;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class CreateRoomResponse {

    private Long roomId;

    private String roomCode;

    public static CreateRoomResponse of(Room room) {
        return CreateRoomResponse.builder()
                .roomId(room.getId())
                .roomCode(room.getCode())
                .build();
    }
}
