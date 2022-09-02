package hous.server.domain.badge.repository;

import hous.server.domain.badge.Represent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepresentRepository extends JpaRepository<Represent, Long>, RepresentRepositoryCustom {
}
