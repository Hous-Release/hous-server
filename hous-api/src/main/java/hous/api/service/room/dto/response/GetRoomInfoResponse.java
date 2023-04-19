package hous.api.service.room.dto.response;

import hous.core.domain.room.Room;
import lombok.*;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class GetRoomInfoResponse {

    private Long roomId;
    private String nickname;

    public static GetRoomInfoResponse of(Room room) {
        if (room == null) {
            return GetRoomInfoResponse.builder()
                    .roomId(null)
                    .nickname(null)
                    .build();
        } else {
            return GetRoomInfoResponse.builder()
                    .roomId(room.getId())
                    .nickname(room.getOwner().getNickname())
                    .build();
        }
    }
}
