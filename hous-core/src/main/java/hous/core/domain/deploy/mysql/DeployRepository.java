package hous.core.domain.deploy.mysql;

import hous.core.domain.deploy.Deploy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeployRepository extends JpaRepository<Deploy, Long>, DeployRepositoryCustom {

}
