package hous.core.domain.todo.mysql;

import org.springframework.data.jpa.repository.JpaRepository;

import hous.core.domain.todo.Done;

public interface DoneRepository extends JpaRepository<Done, Long>, DoneRepositoryCustom {
}
