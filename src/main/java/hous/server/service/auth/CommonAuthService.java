package hous.server.service.auth;

import hous.server.common.util.JwtUtils;
import hous.server.domain.user.User;
import hous.server.domain.user.repository.UserRepository;
import hous.server.service.user.UserServiceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class CommonAuthService {

    private final UserRepository userRepository;

    private final JwtUtils jwtProvider;

    public void logout(Long userId) {
        User user = UserServiceUtils.findUserById(userRepository, userId);
        jwtProvider.expireRefreshToken(user.getId());
        user.resetFcmToken();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void resetConflictFcmToken(String fcmToken) {
        User conflictFcmTokenUser = userRepository.findUserByFcmToken(fcmToken);
        if (conflictFcmTokenUser != null) {
            jwtProvider.expireRefreshToken(conflictFcmTokenUser.getId());
            conflictFcmTokenUser.resetFcmToken();
        }
    }
}
