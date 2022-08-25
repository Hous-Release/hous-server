package hous.server.service.room;

import hous.server.domain.room.Participate;
import hous.server.domain.user.User;
import hous.server.domain.user.repository.UserRepository;
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

    public GetRoomResponse getRoom(Long userId) {
        User user = UserServiceUtils.findUserById(userRepository, userId);
        if (user.getOnboarding().getParticipates().size() == 0) {
            return GetRoomResponse.of(null);
        } else {
            Participate participate = user.getOnboarding().getParticipates().get(0);
            return GetRoomResponse.of(participate.getRoom());
        }
    }
}
