package hous.core.domain.badge.mysql;

import hous.core.domain.badge.Represent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepresentRepository extends JpaRepository<Represent, Long>, RepresentRepositoryCustom {
}
