package hous.core.domain.user.mysql;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SettingRepositoryImpl implements SettingRepositoryCustom {

	private final JPAQueryFactory queryFactory;
}
