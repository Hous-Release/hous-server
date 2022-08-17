package hous.server.common.exception;

public class UnAuthorizedException extends HousException {

    public UnAuthorizedException(String message) {
        super(message, ErrorCode.UNAUTHORIZED_EXCEPTION);
    }
}
