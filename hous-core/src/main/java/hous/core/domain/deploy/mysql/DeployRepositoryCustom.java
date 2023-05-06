package hous.core.domain.deploy.mysql;

import hous.core.domain.deploy.Deploy;

public interface DeployRepositoryCustom {

	Deploy findDeployByOs(String os);
}
