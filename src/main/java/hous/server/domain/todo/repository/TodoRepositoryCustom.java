package hous.server.domain.todo.repository;

import hous.server.domain.todo.Todo;

public interface TodoRepositoryCustom {

    Todo findTodoById(Long id);
}
