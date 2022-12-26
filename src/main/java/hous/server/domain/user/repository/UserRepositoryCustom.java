package hous.server.domain.user.repository;

import hous.server.domain.user.User;
import hous.server.domain.user.UserSocialType;

import java.util.List;

public interface UserRepositoryCustom {

    boolean existsBySocialIdAndSocialType(String socialId, UserSocialType socialType);

    User findUserById(Long id);

    User findUserBySocialIdAndSocialType(String socialId, UserSocialType socialType);

    User findUserByFcmToken(String fcmToken);

    List<User> findAllUsers();

    List<User> findAllUsersByBirthday(String birthday);
}
