package hous.api.service.rule;

import hous.api.service.room.RoomServiceUtils;
import hous.api.service.rule.dto.response.RuleInfoResponse;
import hous.api.service.user.UserServiceUtils;
import hous.core.domain.room.Room;
import hous.core.domain.rule.Rule;
import hous.core.domain.user.User;
import hous.core.domain.user.mysql.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class RuleRetrieveService {

    private final UserRepository userRepository;

    public RuleInfoResponse getRulesInfo(Long userId) {
        User user = UserServiceUtils.findUserById(userRepository, userId);
        Room room = RoomServiceUtils.findParticipatingRoom(user);
        List<Rule> rules = room.getRules();
        return RuleInfoResponse.of(rules);
    }
}
