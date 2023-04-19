package hous.core.domain.todo.mysql;

import com.querydsl.jpa.impl.JPAQueryFactory;
import hous.core.domain.todo.Todo;
import lombok.RequiredArgsConstructor;

import static hous.core.domain.todo.QTodo.todo;

@RequiredArgsConstructor
public class TodoRepositoryImpl implements TodoRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Todo findTodoById(Long id) {
        return queryFactory.selectFrom(todo)
                .where(todo.id.eq(id))
                .fetchOne();
    }
}
