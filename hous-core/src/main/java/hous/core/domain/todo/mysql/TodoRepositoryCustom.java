package hous.core.domain.todo.mysql;

import hous.core.domain.todo.Todo;

public interface TodoRepositoryCustom {

    Todo findTodoById(Long id);
}
