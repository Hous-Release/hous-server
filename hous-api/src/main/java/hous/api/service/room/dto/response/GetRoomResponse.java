package hous.api.service.room.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

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
public class GetRoomResponse {

	private boolean isJoiningRoom;
	private Long roomId;
	private String roomCode;

	@JsonProperty("isJoiningRoom")
	public boolean isJoiningRoom() {
		return isJoiningRoom;
	}

	public static GetRoomResponse of(Room room) {
		if (room == null) {
			return GetRoomResponse.builder()
				.isJoiningRoom(false)
				.roomId(null)
				.roomCode(null)
				.build();
		} else {
			return GetRoomResponse.builder()
				.isJoiningRoom(true)
				.roomId(room.getId())
				.roomCode(room.getCode())
				.build();
		}
	}
}
