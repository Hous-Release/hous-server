package hous.common.exception;

import lombok.Getter;

@Getter
public abstract class HousException extends RuntimeException {

	private final ErrorCode errorCode;

	public HousException(String message, ErrorCode errorCode) {
		super(message);
		this.errorCode = errorCode;
	}

	public int getStatus() {
		return errorCode.getStatus();
	}
}
