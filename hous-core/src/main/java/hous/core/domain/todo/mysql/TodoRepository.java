package hous.core.domain.todo.mysql;

import org.springframework.data.jpa.repository.JpaRepository;

import hous.core.domain.todo.Todo;

public interface TodoRepository extends JpaRepository<Todo, Long>, TodoRepositoryCustom {
}
