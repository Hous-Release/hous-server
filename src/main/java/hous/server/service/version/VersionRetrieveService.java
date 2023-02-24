package hous.server.service.version;

import hous.server.domain.deploy.Deploy;
import hous.server.domain.deploy.mysql.DeployRepository;
import hous.server.service.version.dto.response.VersionInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class VersionRetrieveService {

    private final DeployRepository deployRepository;

    public VersionInfoResponse getVersionInfo(String requestOs, String requestVersion) {
        VersionServiceUtils.validateRequestOs(requestOs);
        VersionServiceUtils.validateRequestVersion(requestVersion);
        Deploy deploy = VersionServiceUtils.findDeployByOs(deployRepository, requestOs);
        String latestVersion = deploy.getVersion();
        boolean needsForceUpdate = VersionServiceUtils.isOutdated(requestVersion, latestVersion);
        return VersionInfoResponse.of(needsForceUpdate, deploy.getMarketUrl());
    }
}
