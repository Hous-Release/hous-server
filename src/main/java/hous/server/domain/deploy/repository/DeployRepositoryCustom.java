package hous.server.domain.deploy.repository;

import hous.server.domain.deploy.Deploy;

public interface DeployRepositoryCustom {

    Deploy findDeployByOs(String os);
}
