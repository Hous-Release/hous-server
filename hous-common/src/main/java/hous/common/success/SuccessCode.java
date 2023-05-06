package hous.common.success;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum SuccessCode {

	/**
	 * 200 OK
	 */
	OK_SUCCESS(SuccessStatusCode.OK, "성공입니다."),

	// 버전
	GET_VERSION_INFO_SUCCESS(SuccessStatusCode.OK, "강제 업데이트 필요 여부 조회 성공입니다."),

	// 인증
	SIGNUP_SUCCESS(SuccessStatusCode.OK, "회원가입 성공입니다."),
	LOGIN_SUCCESS(SuccessStatusCode.OK, "로그인 성공입니다."),
	REISSUE_TOKEN_SUCCESS(SuccessStatusCode.OK, "토큰 갱신 성공입니다."),

	// 방
	GET_ROOM_SUCCESS(SuccessStatusCode.OK, "참가중인 방 조회 성공입니다."),
	GET_ROOM_INFO_SUCCESS(SuccessStatusCode.OK, "참가하려는 방 정보 조회 성공입니다."),
	JOIN_ROOM_SUCCESS(SuccessStatusCode.OK, "방 참여 성공입니다."),

	// 홈
	GET_HOME_INFO_SUCCESS(SuccessStatusCode.OK, "홈 화면 정보 조회 성공입니다."),

	// to-do
	GET_USERS_INFO_SUCCESS(SuccessStatusCode.OK, "담당자 목록 조회 성공입니다."),
	GET_TODO_INFO_SUCCESS(SuccessStatusCode.OK, "todo 정보 조회 성공입니다."),
	GET_TODO_SUMMARY_INFO_SUCCESS(SuccessStatusCode.OK, "todo 요약 정보 조회 성공입니다."),
	GET_TODO_MAIN_SUCCESS(SuccessStatusCode.OK, "todo 메인 페이지 조회 성공입니다."),
	GET_TODO_ALL_DAY_SUCCESS(SuccessStatusCode.OK, "todo 요일별 정보 조회 성공입니다."),
	GET_TODO_ALL_MEMBER_SUCCESS(SuccessStatusCode.OK, "todo 멤버별 정보 조회 성공입니다."),
	GET_MY_TODO_SUCCESS(SuccessStatusCode.OK, "나의 todo 정보 조회 성공입니다."),

	// rule
	GET_RULE_INFO_SUCCESS(SuccessStatusCode.OK, "규칙 조회 성공입니다."),

	// profile
	GET_MY_PROFILE_INFO_SUCCESS(SuccessStatusCode.OK, "나의 프로필 정보 조회 성공입니다."),
	GET_HOMIE_PROFILE_INFO_SUCCESS(SuccessStatusCode.OK, "호미 프로필 정보 조회 성공입니다."),

	// 성향
	GET_PERSONALITY_INFO_SUCCESS(SuccessStatusCode.OK, "성향 정보 조회 성공입니다."),
	GET_PERSONALITY_TEST_INFO_SUCCESS(SuccessStatusCode.OK, "성향테스트 정보 조회 성공입니다."),
	UPDATE_PERSONALITY_TEST_SUCCESS(SuccessStatusCode.OK, "성향테스트 수정 성공입니다."),

	// 배지
	GET_BADGE_INFO_SUCCESS(SuccessStatusCode.OK, "나의 배지 목록 조회 성공입니다."),

	// 알림
	GET_NOTIFICATIONS_INFO_SUCCESS(SuccessStatusCode.OK, "알림 목록 조회 성공입니다."),

	// 설정
	GET_USER_PUSH_SETTING_SUCCESS(SuccessStatusCode.OK, "푸시 알림 설정 정보 조회 성공입니다."),

	/**
	 * 201 CREATED
	 */
	CREATED_SUCCESS(SuccessStatusCode.CREATED, "생성 성공입니다."),

	// 방
	CREATE_ROOM_SUCCESS(SuccessStatusCode.CREATED, "방 생성 성공입니다."),

	// 규칙
	CREATE_RULE_SUCCESS(SuccessStatusCode.CREATED, "규칙 생성 성공입니다.");

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
