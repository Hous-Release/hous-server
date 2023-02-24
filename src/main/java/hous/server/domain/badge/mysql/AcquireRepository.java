package hous.server.domain.badge.mysql;

import hous.server.domain.badge.Acquire;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AcquireRepository extends JpaRepository<Acquire, Long>, AcquireRepositoryCustom {
}
