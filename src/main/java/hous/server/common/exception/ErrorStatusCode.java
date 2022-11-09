package hous.server.common.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum ErrorStatusCode {

    /**
     * error code
     */
    BAD_REQUEST(400),
    UNAUTHORIZED(401),
    FORBIDDEN(403),
    NOT_FOUND(404),
    METHOD_NOT_ALLOWED(405),
    NOT_ACCEPTABLE(406),
    CONFLICT(409),
    UNSUPPORTED_MEDIA_TYPE(415),
    UPGRADE_REQUIRED(426),
    INTERNAL_SERVER(500),
    BAD_GATEWAY(502),
    SERVICE_UNAVAILABLE(503);

    private final int status;
}
