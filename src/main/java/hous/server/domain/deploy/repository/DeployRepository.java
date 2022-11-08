package hous.server.domain.deploy.repository;

import hous.server.domain.deploy.Deploy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeployRepository extends JpaRepository<Deploy, Long>, DeployRepositoryCustom {

}
