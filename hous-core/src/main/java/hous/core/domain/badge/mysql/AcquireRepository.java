package hous.core.domain.badge.mysql;

import org.springframework.data.jpa.repository.JpaRepository;

import hous.core.domain.badge.Acquire;

public interface AcquireRepository extends JpaRepository<Acquire, Long>, AcquireRepositoryCustom {
}
