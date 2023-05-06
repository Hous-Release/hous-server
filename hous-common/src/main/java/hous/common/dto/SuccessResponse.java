package hous.common.dto;

import org.springframework.http.ResponseEntity;

import hous.common.success.SuccessCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SuccessResponse<T> {

	public static final ResponseEntity<SuccessResponse<String>> OK = success(SuccessCode.OK_SUCCESS, null);
	public static final ResponseEntity<SuccessResponse<String>> CREATED = success(SuccessCode.CREATED_SUCCESS, null);

	private int status;
	private boolean success;
	private String message;
	private T data;

	public static <T> ResponseEntity<SuccessResponse<T>> success(SuccessCode successCode, T data) {
		return ResponseEntity
			.status(successCode.getStatus())
			.body(new SuccessResponse<>(successCode.getStatus(), true, successCode.getMessage(), data));
	}
}
