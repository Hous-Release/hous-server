package hous.server.domain.todo.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import hous.server.domain.room.Room;
import lombok.RequiredArgsConstructor;

import static hous.server.domain.todo.QTodo.todo;

@RequiredArgsConstructor
public class TodoRepositoryImpl implements TodoRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public int findCountsByRoom(Room room) {
        return queryFactory.selectFrom(todo)
                .where(todo.room.eq(room))
                .fetch().size();
    }
}
