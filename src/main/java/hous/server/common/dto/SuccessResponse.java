package hous.server.common.dto;

import hous.server.common.success.SuccessCode;
import lombok.*;
import org.springframework.http.ResponseEntity;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SuccessResponse<T> {

    private int status;
    private boolean success;
    private String message;
    private T data;

    public static <T> ResponseEntity<T> success(SuccessCode successCode, T data) {
        return (ResponseEntity<T>) ResponseEntity.status(successCode.getStatus())
                .body(new SuccessResponse(successCode.getStatus(), true, successCode.getMessage(), data));
    }
}
