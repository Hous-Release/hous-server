package hous.api.config.interceptor.version;

import hous.api.service.version.VersionServiceUtils;
import hous.common.exception.UpgradeRequiredException;
import hous.core.domain.deploy.Deploy;
import hous.core.domain.deploy.mysql.DeployRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@Component
public class VersionCheckHandler {

    private final DeployRepository deployRepository;

    public void checkVersion(HttpServletRequest request) {
        String requestOs = request.getHeader("HousOsType");
        String requestVersion = request.getHeader("HousVersion");
        VersionServiceUtils.validateRequestOs(requestOs);
        VersionServiceUtils.validateRequestVersion(requestVersion);
        Deploy deploy = VersionServiceUtils.findDeployByOs(deployRepository, requestOs);
        String latestVersion = deploy.getVersion();
        if (VersionServiceUtils.isOutdated(requestVersion, latestVersion)) {
            throw new UpgradeRequiredException(
                    String.format("업그레이드가 필요한 버전 (%s - %s) 입니다.", requestOs, requestVersion));
        }
    }
}
