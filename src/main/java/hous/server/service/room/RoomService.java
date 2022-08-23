package hous.server.service.room;

import hous.server.domain.room.Participate;
import hous.server.domain.room.Room;
import hous.server.domain.room.repository.ParticipateRepository;
import hous.server.domain.room.repository.RoomRepository;
import hous.server.domain.user.Onboarding;
import hous.server.domain.user.User;
import hous.server.domain.user.repository.UserRepository;
import hous.server.service.room.dto.request.CreateRoomRequestDto;
import hous.server.service.room.dto.response.CreateRoomResponse;
import hous.server.service.user.UserServiceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class RoomService {

    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final ParticipateRepository participateRepository;

    @Transactional
    public CreateRoomResponse createRoom(CreateRoomRequestDto request, Long userId) {
        User user = UserServiceUtils.findUserById(userRepository, userId);
        Onboarding onboarding = user.getOnboarding();
        RoomServiceUtils.validateNotExistsParticipate(participateRepository, onboarding);
        Room room = roomRepository.save(Room.newInstance(request.getName(), RoomServiceUtils.createUniqueRoomCode(roomRepository)));
        Participate participate = participateRepository.save(Participate.newInstance(onboarding, room));
        onboarding.addParticipate(participate);
        room.addParticipate(participate);
        return CreateRoomResponse.of(room);
    }
}
