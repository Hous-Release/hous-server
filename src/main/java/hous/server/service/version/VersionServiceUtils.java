package hous.server.service.version;

import hous.server.common.exception.ErrorCode;
import hous.server.common.exception.NotFoundException;
import hous.server.common.exception.ValidationException;
import hous.server.domain.deploy.Deploy;
import hous.server.domain.deploy.repository.DeployRepository;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VersionServiceUtils {

    public static Deploy findDeployByOs(DeployRepository deployRepository, String os) {
        Deploy deploy = deployRepository.findDeployByOs(os);
        if (deploy == null) {
            throw new NotFoundException(String.format("배포되지 않은 OS (%s) 입니다.", os),
                    ErrorCode.NOT_FOUND_OS_EXCEPTION);
        }
        return deploy;
    }

    public static void validateRequestOs(String requestOs) {
        if (!requestOs.matches("iOS|AOS")) {
            throw new ValidationException(String.format("잘못된 OS 타입 요청 (%s) 입니다.", requestOs),
                    ErrorCode.VALIDATION_OS_EXCEPTION);
        }
    }

    public static void validateRequestVersion(String requestVersion) {
        if (!requestVersion.matches("\\d+\\.\\d+\\.\\d+")) {
            throw new ValidationException(String.format("잘못된 버전 형식 (%s) 입니다.", requestVersion),
                    ErrorCode.VALIDATION_VERSION_EXCEPTION);
        }
    }

    public static boolean isOutdated(String requestVersion, String latestVersion) {
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
