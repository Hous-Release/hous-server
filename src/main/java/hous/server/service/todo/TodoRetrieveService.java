package hous.server.service.todo;

import hous.server.domain.room.Participate;
import hous.server.domain.room.Room;
import hous.server.domain.user.Onboarding;
import hous.server.domain.user.User;
import hous.server.domain.user.repository.UserRepository;
import hous.server.service.todo.dto.response.GetUsersInfoResponse;
import hous.server.service.user.UserServiceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class TodoRetrieveService {

    private final UserRepository userRepository;

    public GetUsersInfoResponse getUsersInfo(Long userId) {
        User user = UserServiceUtils.findUserById(userRepository, userId);
        Participate participate = user.getOnboarding().getParticipates().get(0);
        Room room = participate.getRoom();
        List<Participate> participates = room.getParticipates();
        List<Onboarding> onboardings = participates.stream()
                .map(Participate::getOnboarding)
                .collect(Collectors.toList());
        return GetUsersInfoResponse.of(onboardings);
    }
}
