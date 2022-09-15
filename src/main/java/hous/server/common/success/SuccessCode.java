package hous.server.common.success;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static hous.server.common.success.SuccessStatusCode.*;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum SuccessCode {

    /**
     * 200 OK
     */
    // 인증
    LOGIN_SUCCESS(OK, "로그인 성공입니다."),
    REISSUE_TOKEN_SUCCESS(OK, "토큰 갱신 성공입니다."),

    // 온보딩
    CHECK_ONBOARDING_SUCCESS(OK, "온보딩 등록여부 조회 성공입니다."),

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

    // 뱃지
    GET_BADGE_INFO_SUCCESS(OK, "나의 뱃지 목록 조회 성공입니다."),

    /**
     * 201 CREATED
     */

    // 방
    CREATE_ROOM_SUCCESS(CREATED, "방 생성 성공입니다."),

    // 규칙
    CREATE_RULE_SUCCESS(CREATED, "규칙 생성 성공입니다."),

    /**
     * 202 ACCEPTED
     */

    /**
     * 204 NO_CONTENT
     */
    NO_CONTENT_SUCCESS(NO_CONTENT, "");

    private final SuccessStatusCode statusCode;
    private final String message;

    public int getStatus() {
        return statusCode.getStatus();
    }
}
