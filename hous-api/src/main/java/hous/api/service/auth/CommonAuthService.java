package hous.api.service.auth;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import hous.api.service.user.UserServiceUtils;
import hous.common.util.JwtUtils;
import hous.core.domain.user.User;
import hous.core.domain.user.mysql.UserRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional
public class CommonAuthService {

	private final UserRepository userRepository;

	private final JwtUtils jwtUtils;

	public void logout(Long userId) {
		User user = UserServiceUtils.findUserById(userRepository, userId);
		jwtUtils.expireRefreshToken(user.getId());
		user.resetFcmToken();
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void resetConflictFcmToken(String fcmToken) {
		User conflictFcmTokenUser = userRepository.findUserByFcmToken(fcmToken);
		if (conflictFcmTokenUser != null) {
			jwtUtils.expireRefreshToken(conflictFcmTokenUser.getId());
			conflictFcmTokenUser.resetFcmToken();
		}
	}
}
