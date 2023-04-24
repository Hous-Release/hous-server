package hous.core.domain.room.mysql;

import static hous.core.domain.room.QRoom.*;

import com.querydsl.jpa.impl.JPAQueryFactory;

import hous.core.domain.room.Room;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RoomRepositoryImpl implements RoomRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public boolean existsByRoomCode(String code) {
		return queryFactory.selectOne()
			.from(room)
			.where(room.code.eq(code))
			.fetchFirst() != null;
	}

	@Override
	public Room findRoomById(Long id) {
		return queryFactory
			.selectFrom(room)
			.where(room.id.eq(id))
			.fetchOne();
	}

	@Override
	public Room findRoomByCode(String code) {
		return queryFactory
			.selectFrom(room)
			.where(room.code.eq(code))
			.fetchOne();
	}

}
