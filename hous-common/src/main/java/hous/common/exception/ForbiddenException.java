package hous.common.exception;

public class ForbiddenException extends HousException {

	public ForbiddenException(String message, ErrorCode errorCode) {
		super(message, errorCode);
	}

	public ForbiddenException(String message) {
		super(message, ErrorCode.FORBIDDEN_EXCEPTION);
	}
}
