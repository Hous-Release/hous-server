package hous.core.domain.badge.mysql;

import hous.core.domain.badge.Acquire;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AcquireRepository extends JpaRepository<Acquire, Long>, AcquireRepositoryCustom {
}
