package hous.server.common.dto;

import hous.server.common.success.SuccessCode;
import lombok.*;
import org.springframework.http.ResponseEntity;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SuccessResponse<T> {

    public static final ResponseEntity<String> OK = success(SuccessCode.OK_SUCCESS, null);
    public static final ResponseEntity<String> CREATED = success(SuccessCode.CREATED_SUCCESS, null);
    public static final ResponseEntity<String> NO_CONTENT = success(SuccessCode.NO_CONTENT_SUCCESS, null);

    private int status;
    private boolean success;
    private String message;
    private T data;

    public static <T> ResponseEntity<T> success(SuccessCode successCode, T data) {
        return (ResponseEntity<T>) ResponseEntity.status(successCode.getStatus())
                .body(new SuccessResponse(successCode.getStatus(), true, successCode.getMessage(), data));
    }
}
