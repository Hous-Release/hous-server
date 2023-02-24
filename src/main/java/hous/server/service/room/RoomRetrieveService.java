package hous.server.service.room;

import hous.server.domain.room.Participate;
import hous.server.domain.room.Room;
import hous.server.domain.room.mysql.RoomRepository;
import hous.server.domain.user.User;
import hous.server.domain.user.mysql.UserRepository;
import hous.server.service.room.dto.response.GetRoomInfoResponse;
import hous.server.service.room.dto.response.GetRoomResponse;
import hous.server.service.user.UserServiceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
