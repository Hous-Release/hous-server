package hous.server.service.auth.impl;

import hous.server.domain.user.User;
import hous.server.domain.user.UserSocialType;
import hous.server.domain.user.repository.UserRepository;
import hous.server.external.client.apple.AppleTokenProvider;
import hous.server.service.auth.AuthService;
import hous.server.service.auth.dto.request.LoginDto;
import hous.server.service.user.UserService;
import hous.server.service.user.UserServiceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AppleAuthService implements AuthService {

    private static final UserSocialType socialType = UserSocialType.APPLE;

    private final AppleTokenProvider appleTokenDecoder;

    private final UserService userService;
    private final UserRepository userRepository;

    @Override
    public Long login(LoginDto request) {
        String socialId = appleTokenDecoder.getSocialIdFromIdToken(request.getToken());
        User user = UserServiceUtils.findUserBySocialIdAndSocialType(userRepository, socialId, socialType);
        if (user == null) return userService.registerUser(request.toCreateUserDto(socialId));
        else user.updateFcmToken(request.getFcmToken());
        return user.getId();
    }
}
