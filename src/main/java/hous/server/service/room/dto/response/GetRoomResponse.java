package hous.server.service.room.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import hous.server.domain.room.Room;
import lombok.*;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class GetRoomResponse {

    private boolean isJoiningRoom;
    private Long roomId;

    @JsonProperty("isJoiningRoom")
    public boolean isJoiningRoom() {
        return isJoiningRoom;
    }

    public static GetRoomResponse of(Room room) {
        if (room == null) {
            return GetRoomResponse.builder()
                    .isJoiningRoom(false)
                    .roomId(null)
                    .build();
        } else {
            return GetRoomResponse.builder()
                    .isJoiningRoom(true)
                    .roomId(room.getId())
                    .build();
        }
    }
}
