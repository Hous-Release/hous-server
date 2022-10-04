package hous.server.common.dto;

import hous.server.common.success.SuccessCode;
import lombok.*;
import org.springframework.http.ResponseEntity;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SuccessResponse<T> {

    public static final ResponseEntity<SuccessResponse<String>> OK = success(SuccessCode.OK_SUCCESS, null);
    public static final ResponseEntity<SuccessResponse<String>> CREATED = success(SuccessCode.CREATED_SUCCESS, null);
    public static final ResponseEntity<SuccessResponse<String>> NO_CONTENT = success(SuccessCode.NO_CONTENT_SUCCESS, null);

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
