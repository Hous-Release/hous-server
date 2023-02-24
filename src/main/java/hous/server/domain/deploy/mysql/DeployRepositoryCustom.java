package hous.server.domain.deploy.mysql;

import hous.server.domain.deploy.Deploy;

public interface DeployRepositoryCustom {

    Deploy findDeployByOs(String os);
}
