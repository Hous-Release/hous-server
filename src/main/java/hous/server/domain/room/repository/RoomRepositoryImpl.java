package hous.server.domain.room.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import static hous.server.domain.room.QRoom.room;

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
}
