package hous.core.domain.todo.mysql;

import hous.core.domain.todo.Done;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoneRepository extends JpaRepository<Done, Long>, DoneRepositoryCustom {
}
