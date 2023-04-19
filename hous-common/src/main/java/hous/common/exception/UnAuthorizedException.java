package hous.common.exception;

public class UnAuthorizedException extends HousException {

    public UnAuthorizedException(String message) {
        super(message, ErrorCode.UNAUTHORIZED_EXCEPTION);
    }

    public UnAuthorizedException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
