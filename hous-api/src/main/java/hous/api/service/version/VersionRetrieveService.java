package hous.api.service.version;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hous.api.service.version.dto.response.VersionInfoResponse;
import hous.core.domain.deploy.Deploy;
import hous.core.domain.deploy.mysql.DeployRepository;
import lombok.RequiredArgsConstructor;

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
