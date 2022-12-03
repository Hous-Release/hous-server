package hous.server.config.interceptor.version;

import hous.server.common.exception.ErrorCode;
import hous.server.common.exception.NotFoundException;
import hous.server.common.exception.UpgradeRequiredException;
import hous.server.common.exception.ValidationException;
import hous.server.domain.deploy.Deploy;
import hous.server.domain.deploy.repository.DeployRepository;
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
        validateRequestOs(requestOs);
        validateRequestVersion(requestVersion);
        Deploy deploy = deployRepository.findDeployByOs(requestOs);
        if (deploy == null) {
            throw new NotFoundException(String.format("배포되지 않은 OS (%s) 입니다.", requestOs),
                    ErrorCode.NOT_FOUND_OS_EXCEPTION);
        }
        String latestVersion = deploy.getVersion();
        if (isOutdated(requestVersion, latestVersion)) {
            throw new UpgradeRequiredException(
                    String.format("업그레이드가 필요한 버전 (%s - %s) 입니다.", requestOs, requestVersion));
        }
    }

    private void validateRequestOs(String requestOs) {
        if (!requestOs.matches("iOS|AOS")) {
            throw new ValidationException(String.format("잘못된 OS 타입 요청 (%s) 입니다.", requestOs),
                    ErrorCode.VALIDATION_OS_EXCEPTION);
        }
    }

    private void validateRequestVersion(String requestVersion) {
        if (!requestVersion.matches("\\d+\\.\\d+\\.\\d+")) {
            throw new ValidationException(String.format("잘못된 버전 형식 (%s) 입니다.", requestVersion),
                    ErrorCode.VALIDATION_VERSION_EXCEPTION);
        }
    }

    private boolean isOutdated(String requestVersion, String latestVersion) {
        String[] request = requestVersion.split("\\.");
        String[] latest = latestVersion.split("\\.");
        if (Integer.parseInt(request[0]) < Integer.parseInt(latest[0])) {
            return true;
        }
        if (Integer.parseInt(request[1]) < Integer.parseInt(latest[1])) {
            return true;
        }
        return false;
    }
}
