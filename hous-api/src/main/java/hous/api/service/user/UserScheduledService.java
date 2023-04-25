package hous.api.service.user;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hous.api.service.badge.BadgeService;
import hous.common.util.DateUtils;
import hous.core.domain.badge.BadgeInfo;
import hous.core.domain.user.User;
import hous.core.domain.user.mysql.UserRepository;
import lombok.RequiredArgsConstructor;

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
