package hous.server.service.user;

import hous.server.common.util.DateUtils;
import hous.server.domain.badge.BadgeInfo;
import hous.server.domain.user.User;
import hous.server.domain.user.repository.UserRepository;
import hous.server.service.badge.BadgeService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class UserScheduledService {

    private final UserRepository userRepository;

    private final BadgeService badgeService;

    /**
     * 매일 9시 0분 0초마다 실행
     */
    @Scheduled(cron = "0  0  9  *  *  *")
    public void scheduledTodayBirthday() {
        List<User> users = userRepository.findAllUsersByBirthday(DateUtils.todayLocalDateToString());
        users.forEach(user -> badgeService.acquireBadge(user, BadgeInfo.HOMIE_IS_BORN));
    }
}
