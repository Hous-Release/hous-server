package hous.server.domain.todo.mysql;

import hous.server.domain.todo.Redo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RedoRepository extends JpaRepository<Redo, Long>, RedoRepositoryCustom {
}
