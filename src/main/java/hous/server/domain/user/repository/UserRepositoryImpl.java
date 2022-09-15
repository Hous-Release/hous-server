package hous.server.domain.user.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import hous.server.domain.user.User;
import hous.server.domain.user.UserSocialType;
import hous.server.domain.user.UserStatus;
import lombok.RequiredArgsConstructor;

import static hous.server.domain.user.QUser.user;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public boolean existsBySocialIdAndSocialType(String socialId, UserSocialType socialType) {
        return queryFactory.selectOne()
                .from(user)
                .where(
                        user.socialInfo.socialId.eq(socialId),
                        user.socialInfo.socialType.eq(socialType),
                        user.status.eq(UserStatus.ACTIVE)
                ).fetchFirst() != null;
    }

    @Override
    public User findUserById(Long id) {
        return queryFactory
                .selectFrom(user)
                .where(
                        user.id.eq(id),
                        user.status.eq(UserStatus.ACTIVE)
                )
                .fetchOne();
    }

    @Override
    public User findUserBySocialIdAndSocialType(String socialId, UserSocialType socialType) {
        return queryFactory
                .selectFrom(user)
                .where(
                        user.socialInfo.socialId.eq(socialId),
                        user.socialInfo.socialType.eq(socialType),
                        user.status.eq(UserStatus.ACTIVE)
                )
                .fetchOne();
    }

    @Override
    public User findUserByFcmToken(String fcmToken) {
        return queryFactory
                .selectFrom(user)
                .where(
                        user.fcmToken.eq(fcmToken),
                        user.status.eq(UserStatus.ACTIVE)
                )
                .fetchOne();
    }
}
