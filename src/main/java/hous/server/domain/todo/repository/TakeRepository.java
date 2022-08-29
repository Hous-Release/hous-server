package hous.server.domain.todo.repository;

import hous.server.domain.todo.Take;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TakeRepository extends JpaRepository<Take, Long>, TakeRepositoryCustom {
}
