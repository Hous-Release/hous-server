package hous.server.common.success;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static hous.server.common.success.SuccessStatusCode.CREATED;
import static hous.server.common.success.SuccessStatusCode.OK;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum SuccessCode {

    /**
     * 200 OK
     */
    SUCCESS(OK, "성공입니다."),

    // 인증
    LOGIN_SUCCESS(OK, "로그인 성공입니다."),
    REISSUE_TOKEN_SUCCESS(OK, "토큰 갱신 성공입니다."),

    GET_ROOM_SUCCESS(OK, "참가중인 방 조회 성공입니다."),
    JOIN_ROOM_SUCCESS(OK, "방 참여 성공입니다."),

    /**
     * 201 CREATED
     */
    CREATE_ROOM_SUCCESS(CREATED, "방 생성 성공입니다."),

    /**
     * 202 ACCEPTED
     */

    /**
     * 204 NO_CONTENT
     */
    ;

    private final SuccessStatusCode statusCode;
    private final String message;

    public int getStatus() {
        return statusCode.getStatus();
    }
}
