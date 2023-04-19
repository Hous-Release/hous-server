package hous.api.service.auth;

import hous.api.service.jwt.JwtService;
import hous.api.service.user.UserServiceUtils;
import hous.core.domain.user.User;
import hous.core.domain.user.mysql.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class CommonAuthService {

    private final UserRepository userRepository;

    private final JwtService jwtService;

    public void logout(Long userId) {
        User user = UserServiceUtils.findUserById(userRepository, userId);
        jwtService.expireRefreshToken(user.getId());
        user.resetFcmToken();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void resetConflictFcmToken(String fcmToken) {
        User conflictFcmTokenUser = userRepository.findUserByFcmToken(fcmToken);
        if (conflictFcmTokenUser != null) {
            jwtService.expireRefreshToken(conflictFcmTokenUser.getId());
            conflictFcmTokenUser.resetFcmToken();
        }
    }
}
