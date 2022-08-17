package hous.server.service.user;

import hous.server.common.exception.ConflictException;
import hous.server.common.exception.NotFoundException;
import hous.server.domain.user.User;
import hous.server.domain.user.UserSocialType;
import hous.server.domain.user.repository.UserRepository;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static hous.server.common.exception.ErrorCode.CONFLICT_USER_EXCEPTION;
import static hous.server.common.exception.ErrorCode.NOT_FOUND_USER_EXCEPTION;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserServiceUtils {

    static void validateNotExistsUser(UserRepository userRepository, String socialId, UserSocialType socialType) {
        if (userRepository.existsBySocialIdAndSocialType(socialId, socialType)) {
            throw new ConflictException(String.format("이미 존재하는 유저 (%s - %s) 입니다", socialId, socialType), CONFLICT_USER_EXCEPTION);
        }
    }

    public static User findUserBySocialIdAndSocialType(UserRepository userRepository, String socialId, UserSocialType socialType) {
        return userRepository.findUserBySocialIdAndSocialType(socialId, socialType);
    }

    public static User findUserById(UserRepository userRepository, Long userId) {
        User user = userRepository.findUserById(userId);
        if (user == null) {
            throw new NotFoundException(String.format("존재하지 않는 유저 (%s) 입니다", userId), NOT_FOUND_USER_EXCEPTION);
        }
        return user;
    }
}
