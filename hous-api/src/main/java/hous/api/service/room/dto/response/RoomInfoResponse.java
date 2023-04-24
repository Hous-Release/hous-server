package hous.api.service.room.dto.response;

import hous.core.domain.room.Room;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
