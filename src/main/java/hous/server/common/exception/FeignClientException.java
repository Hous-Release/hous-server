package hous.server.common.exception;

import lombok.Getter;

@Getter
public class FeignClientException extends RuntimeException {

    private final int status;
    private final String errorMessage;

    public FeignClientException(int status, String errorMessage) {
        this.status = status;
        this.errorMessage = errorMessage;
    }
}
