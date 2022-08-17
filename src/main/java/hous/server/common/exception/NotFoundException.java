package hous.server.common.exception;

public class NotFoundException extends HousException {

    public NotFoundException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public NotFoundException(String message) {
        super(message, ErrorCode.NOT_FOUND_EXCEPTION);
    }
}
