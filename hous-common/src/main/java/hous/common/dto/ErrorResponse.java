package hous.common.dto;

import hous.common.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorResponse {

	private int status;
	private boolean success;
	private String message;

	public static ErrorResponse error(ErrorCode errorCode) {
		return new ErrorResponse(errorCode.getStatus(), false, errorCode.getMessage());
	}

	public static ErrorResponse error(ErrorCode errorCode, String message) {
		return new ErrorResponse(errorCode.getStatus(), false, message);
	}
}
