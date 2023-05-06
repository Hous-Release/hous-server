package hous.core.domain.user.mysql;

import static hous.core.domain.user.QUser.*;

import java.util.List;

import com.querydsl.jpa.impl.JPAQueryFactory;

import hous.core.domain.user.User;
import hous.core.domain.user.UserSocialType;
import hous.core.domain.user.UserStatus;
import lombok.RequiredArgsConstructor;

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

	@Override
	public List<User> findAllUsers() {
		return queryFactory
			.selectFrom(user)
			.where(user.status.eq(UserStatus.ACTIVE))
			.fetch();
	}

	@Override
	public List<User> findAllUsersByBirthday(String birthday) {
		return queryFactory
			.selectFrom(user)
			.where(
				user.status.eq(UserStatus.ACTIVE),
				user.onboarding.birthday.eq(birthday)
			)
			.fetch();
	}
}
