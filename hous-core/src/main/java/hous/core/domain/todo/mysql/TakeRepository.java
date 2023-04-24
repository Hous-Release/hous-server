package hous.core.domain.todo.mysql;

import org.springframework.data.jpa.repository.JpaRepository;

import hous.core.domain.todo.Take;

public interface TakeRepository extends JpaRepository<Take, Long>, TakeRepositoryCustom {
}
