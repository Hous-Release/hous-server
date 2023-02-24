package hous.server.domain.todo.mysql;

import hous.server.domain.todo.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo, Long>, TodoRepositoryCustom {
}
