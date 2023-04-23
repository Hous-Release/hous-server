package hous.core.domain.todo.mysql;

import hous.core.domain.todo.Take;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TakeRepository extends JpaRepository<Take, Long>, TakeRepositoryCustom {
}
