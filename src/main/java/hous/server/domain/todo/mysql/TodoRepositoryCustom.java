package hous.server.domain.todo.mysql;

import hous.server.domain.todo.Todo;

public interface TodoRepositoryCustom {

    Todo findTodoById(Long id);
}
