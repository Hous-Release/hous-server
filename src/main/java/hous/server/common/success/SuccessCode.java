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
    OK_SUCCESS(OK, "성공입니다."),

    // 인증
    SIGNUP_SUCCESS(OK, "회원가입 성공입니다."),
    LOGIN_SUCCESS(OK, "로그인 성공입니다."),
    REISSUE_TOKEN_SUCCESS(OK, "토큰 갱신 성공입니다."),

    // 방
    GET_ROOM_SUCCESS(OK, "참가중인 방 조회 성공입니다."),
    GET_ROOM_INFO_SUCCESS(OK, "참가하려는 방 정보 조회 성공입니다."),
    JOIN_ROOM_SUCCESS(OK, "방 참여 성공입니다."),

    // 홈
    GET_HOME_INFO_SUCCESS(OK, "홈 화면 정보 조회 성공입니다."),

    // to-do
    GET_USERS_INFO_SUCCESS(OK, "담당자 목록 조회 성공입니다."),
    GET_TODO_INFO_SUCCESS(OK, "todo 정보 조회 성공입니다."),
    GET_TODO_SUMMARY_INFO_SUCCESS(OK, "todo 요약 정보 조회 성공입니다."),
    GET_TODO_MAIN_SUCCESS(OK, "todo 메인 페이지 조회 성공입니다."),
    GET_TODO_ALL_DAY_SUCCESS(OK, "todo 요일별 정보 조회 성공입니다."),
    GET_TODO_ALL_MEMBER_SUCCESS(OK, "todo 멤버별 정보 조회 성공입니다."),
    GET_MY_TODO_SUCCESS(OK, "나의 todo 정보 조회 성공입니다."),

    // rule
    GET_RULE_INFO_SUCCESS(OK, "규칙 조회 성공입니다."),

    // profile
    GET_MY_PROFILE_INFO_SUCCESS(OK, "나의 프로필 정보 조회 성공입니다."),
    GET_HOMIE_PROFILE_INFO_SUCCESS(OK, "호미 프로필 정보 조회 성공입니다."),

    // 성향
    GET_PERSONALITY_INFO_SUCCESS(OK, "성향 정보 조회 성공입니다."),
    GET_PERSONALITY_TEST_INFO_SUCCESS(OK, "성향테스트 정보 조회 성공입니다."),
    UPDATE_PERSONALITY_TEST_SUCCESS(OK, "성향테스트 수정 성공입니다."),

    // 배지
    GET_BADGE_INFO_SUCCESS(OK, "나의 배지 목록 조회 성공입니다."),

    // 알림
    GET_NOTIFICATIONS_INFO_SUCCESS(OK, "알림 목록 조회 성공입니다."),

    // 설정
    GET_USER_PUSH_SETTING_SUCCESS(OK, "푸시 알림 설정 정보 조회 성공입니다."),

    /**
     * 201 CREATED
     */
    CREATED_SUCCESS(CREATED, "생성 성공입니다."),

    // 방
    CREATE_ROOM_SUCCESS(CREATED, "방 생성 성공입니다."),

    // 규칙
    CREATE_RULE_SUCCESS(CREATED, "규칙 생성 성공입니다.");

    /**
     * 202 ACCEPTED
     */

    /**
     * 204 NO_CONTENT
     */

    private final SuccessStatusCode statusCode;
    private final String message;

    public int getStatus() {
        return statusCode.getStatus();
    }
}
