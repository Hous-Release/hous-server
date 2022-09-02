package hous.server.service.user;

import hous.server.domain.badge.Represent;
import hous.server.domain.badge.repository.RepresentRepository;
import hous.server.domain.user.Onboarding;
import hous.server.domain.user.User;
import hous.server.domain.user.repository.UserRepository;
import hous.server.service.user.dto.response.CheckOnboardingInfoResponse;
import hous.server.service.user.dto.response.UserInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class UserRetrieveService {

    private final UserRepository userRepository;
    private final RepresentRepository representRepository;

    public CheckOnboardingInfoResponse checkMyOnboardingInfo(Long userId) {
        User user = UserServiceUtils.findUserById(userRepository, userId);
        Onboarding onboarding = user.getOnboarding();
        return !onboarding.isChecked() ? CheckOnboardingInfoResponse.of(false) : CheckOnboardingInfoResponse.of(true);
    }

    public UserInfoResponse getUserInfo(Long userId) {
        User user = UserServiceUtils.findUserById(userRepository, userId);
        Onboarding onboarding = user.getOnboarding();
        Represent represent = representRepository.findRepresentByOnboarding(onboarding);
        return UserInfoResponse.of(onboarding, represent);
    }


}
