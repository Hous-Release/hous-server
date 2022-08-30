package hous.server.service.todo;

import hous.server.domain.room.Participate;
import hous.server.domain.room.Room;
import hous.server.domain.todo.repository.DoneRepository;
import hous.server.domain.user.Onboarding;
import hous.server.domain.user.User;
import hous.server.domain.user.repository.UserRepository;
import hous.server.service.room.RoomServiceUtils;
import hous.server.service.todo.dto.response.GetTodoMainResponse;
import hous.server.service.todo.dto.response.GetUsersInfoResponse;
import hous.server.service.user.UserServiceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class TodoRetrieveService {

    private final UserRepository userRepository;
    private final DoneRepository doneRepository;

    public GetUsersInfoResponse getUsersInfo(Long userId) {
        User user = UserServiceUtils.findUserById(userRepository, userId);
        Room room = RoomServiceUtils.findParticipatingRoom(user);
        List<Participate> participates = room.getParticipates();
        List<Onboarding> onboardings = participates.stream()
                .map(Participate::getOnboarding)
                .sorted(Comparator.comparing(onboarding -> onboarding.getTestScore().getCreatedAt()))
                .collect(Collectors.toList());
        return GetUsersInfoResponse.of(onboardings);
    }

    public GetTodoMainResponse getTodoMain(Long userId) {
        User user = UserServiceUtils.findUserById(userRepository, userId);
        Room room = RoomServiceUtils.findParticipatingRoom(user);
        return GetTodoMainResponse.of(user.getOnboarding(), LocalDate.now(ZoneId.of("Asia/Seoul")), room.getTodos(), doneRepository);
    }
}
