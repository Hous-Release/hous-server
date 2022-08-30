package hous.server.domain.todo.repository;

import hous.server.domain.todo.Done;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoneRepository extends JpaRepository<Done, Long>, DoneRepositoryCustom {
}
