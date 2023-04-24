package hous.core.domain.badge.mysql;

import org.springframework.data.jpa.repository.JpaRepository;

import hous.core.domain.badge.Represent;

public interface RepresentRepository extends JpaRepository<Represent, Long>, RepresentRepositoryCustom {
}
