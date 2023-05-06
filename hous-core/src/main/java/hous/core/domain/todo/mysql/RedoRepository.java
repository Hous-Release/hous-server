package hous.core.domain.todo.mysql;

import org.springframework.data.jpa.repository.JpaRepository;

import hous.core.domain.todo.Redo;

public interface RedoRepository extends JpaRepository<Redo, Long>, RedoRepositoryCustom {
}
