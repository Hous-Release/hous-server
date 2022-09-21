package hous.server.service.auth.impl;

import hous.server.common.util.HttpHeaderUtils;
import hous.server.domain.user.User;
import hous.server.domain.user.UserSocialType;
import hous.server.domain.user.repository.UserRepository;
import hous.server.external.client.kakao.KakaoApiClient;
import hous.server.external.client.kakao.dto.response.KakaoProfileResponse;
import hous.server.service.auth.AuthService;
import hous.server.service.auth.dto.request.LoginDto;
import hous.server.service.user.UserService;
import hous.server.service.user.UserServiceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class KaKaoAuthService implements AuthService {

    private static final UserSocialType socialType = UserSocialType.KAKAO;

    private final KakaoApiClient kaKaoApiCaller;

    private final UserRepository userRepository;

    private final UserService userService;

    @Override
    public Long login(LoginDto request) {
        KakaoProfileResponse response = kaKaoApiCaller.getProfileInfo(HttpHeaderUtils.withBearerToken(request.getToken()));
        User user = UserServiceUtils.findUserBySocialIdAndSocialType(userRepository, response.getId(), socialType);
        if (user == null) return userService.registerUser(request.toCreateUserDto(response.getId()));
        else {
            UserServiceUtils.validateUniqueFcmToken(userRepository, request.getFcmToken());
            user.updateFcmToken(request.getFcmToken());
        }
        return user.getId();
    }
}
