package hous.server.common.dto;

import feign.Response;
import hous.server.common.success.SuccessCode;
import lombok.*;
import org.springframework.http.ResponseEntity;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SuccessResponse {

    private int status;
    private boolean success;
    private String message;
    private Object data;

    public static ResponseEntity<SuccessResponse> success(SuccessCode successCode, Object data) {
        return ResponseEntity.status(successCode.getStatus())
                .body(new SuccessResponse(successCode.getStatus(), true, successCode.getMessage(), data));
    }
}
