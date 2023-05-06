package hous.api.service.room;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hous.api.service.room.dto.response.GetRoomInfoResponse;
import hous.api.service.room.dto.response.GetRoomResponse;
import hous.api.service.user.UserServiceUtils;
import hous.core.domain.room.Participate;
import hous.core.domain.room.Room;
import hous.core.domain.room.mysql.RoomRepository;
import hous.core.domain.user.User;
import hous.core.domain.user.mysql.UserRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class RoomRetrieveService {

	private final UserRepository userRepository;
	private final RoomRepository roomRepository;

	public GetRoomResponse getRoom(Long userId) {
		User user = UserServiceUtils.findUserById(userRepository, userId);
		if (user.getOnboarding().getParticipates().size() == 0) {
			return GetRoomResponse.of(null);
		} else {
			Participate participate = user.getOnboarding().getParticipates().get(0);
			return GetRoomResponse.of(participate.getRoom());
		}
	}

	public GetRoomInfoResponse getRoomInfo(String code) {
		Room room = roomRepository.findRoomByCode(code);
		return GetRoomInfoResponse.of(room);
	}
}
