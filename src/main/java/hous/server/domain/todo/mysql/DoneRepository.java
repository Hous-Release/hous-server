package hous.server.domain.todo.mysql;

import hous.server.domain.todo.Done;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoneRepository extends JpaRepository<Done, Long>, DoneRepositoryCustom {
}
