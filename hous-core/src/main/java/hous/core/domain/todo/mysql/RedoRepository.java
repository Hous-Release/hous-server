package hous.core.domain.todo.mysql;

import hous.core.domain.todo.Redo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RedoRepository extends JpaRepository<Redo, Long>, RedoRepositoryCustom {
}
