package hous.server.domain.deploy.mysql;

import hous.server.domain.deploy.Deploy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeployRepository extends JpaRepository<Deploy, Long>, DeployRepositoryCustom {

}
