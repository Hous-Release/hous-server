package hous.server.common.exception;

public class UpgradeRequiredException extends HousException {

    public UpgradeRequiredException(String message) {
        super(message, ErrorCode.UPGRADE_REQUIRED_EXCEPTION);
    }
}
