package hous.server.service.user;

import hous.server.domain.personality.PersonalityColor;
import hous.server.domain.personality.repository.PersonalityRepository;
import hous.server.domain.user.Onboarding;
import hous.server.domain.user.Setting;
import hous.server.domain.user.User;
import hous.server.domain.user.repository.UserRepository;
import hous.server.service.user.dto.request.CreateUserDto;
import hous.server.service.user.dto.request.SetOnboardingInfoRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PersonalityRepository personalityRepository;

    public Long registerUser(CreateUserDto request) {
        System.out.println(personalityRepository.findPersonalityByColor(PersonalityColor.GRAY));
        UserServiceUtils.validateNotExistsUser(userRepository, request.getSocialId(), request.getSocialType());
        User user = userRepository.save(User.newInstance(request.getSocialId(), request.getSocialType(), request.getFcmToken(),
                Onboarding.newInstance(personalityRepository.findPersonalityByColor(PersonalityColor.GRAY)), Setting.newInstance()));
        return user.getId();
    }

    public void setOnboardingInfo(SetOnboardingInfoRequestDto request, Long userId) {
        User user = UserServiceUtils.findUserById(userRepository, userId);
        Onboarding onboarding = user.getOnboarding();
        onboarding.setOnboarding(request.getNickname(), request.getBirthday());
    }
}
