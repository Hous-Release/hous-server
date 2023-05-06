package hous.core.domain.deploy.mysql;

import org.springframework.data.jpa.repository.JpaRepository;

import hous.core.domain.deploy.Deploy;

public interface DeployRepository extends JpaRepository<Deploy, Long>, DeployRepositoryCustom {

}
