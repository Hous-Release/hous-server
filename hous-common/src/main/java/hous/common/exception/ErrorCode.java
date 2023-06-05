package hous.common.exception;

import static hous.common.exception.ErrorStatusCode.*;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum ErrorCode {

	/**
	 * 400 Bad Request
	 */
	VALIDATION_EXCEPTION(BAD_REQUEST, "잘못된 요청입니다."),
	VALIDATION_ENUM_VALUE_EXCEPTION(BAD_REQUEST, "잘못된 Enum 값 입니다."),
	VALIDATION_REQUEST_MISSING_EXCEPTION(BAD_REQUEST, "필수적인 요청 값이 입력되지 않았습니다."),
	VALIDATION_WRONG_TYPE_EXCEPTION(BAD_REQUEST, "잘못된 타입이 입력되었습니다."),
	VALIDATION_SOCIAL_TYPE_EXCEPTION(BAD_REQUEST, "잘못된 소셜 프로바이더 입니다."),
	VALIDATION_SORT_TYPE_EXCEPTION(BAD_REQUEST, "허용하지 않는 정렬기준을 입력했습니다."),
	VALIDATION_OS_EXCEPTION(BAD_REQUEST, "잘못된 OS 타입 요청입니다."),
	VALIDATION_VERSION_EXCEPTION(BAD_REQUEST, "잘못된 버전 형식입니다."),
	VALIDATION_STATUS_EXCEPTION(BAD_REQUEST, "잘못된 상태로 요청했습니다."),
	VALIDATION_RULE_MAX_LENGTH_EXCEPTION(BAD_REQUEST, "규칙은 20 글자 이내로 입력해주세요."),
	VALIDATION_RULE_MIN_LENGTH_EXCEPTION(BAD_REQUEST, "규칙 내용을 입력해주세요."),
	VALIDATION_BIRTHDAY_EXCEPTION(BAD_REQUEST, "생년월일이 없는 경우 공개 여부는 true가 될 수 없습니다."),
	VALIDATION_RULE_IMAGE_MAX_COUNT_EXCEPTION(BAD_REQUEST, "규칙 이미지는 최대 5 개 입니다."),
	VALIDATION_IMAGE_SIZE_EXCEPTION(BAD_REQUEST, "이미지가 (720x720) 보다 큽니다."),

	/**
	 * 401 UnAuthorized
	 */
	UNAUTHORIZED_EXCEPTION(UNAUTHORIZED, "토큰이 만료되었습니다. 다시 로그인 해주세요."),
	UNAUTHORIZED_INVALID_TOKEN_EXCEPTION(UNAUTHORIZED, "유효하지 않은 토큰입니다."),

	/**
	 * 403 Forbidden
	 */
	FORBIDDEN_EXCEPTION(FORBIDDEN, "허용하지 않는 요청입니다."),
	FORBIDDEN_FILE_TYPE_EXCEPTION(BAD_REQUEST, "허용되지 않은 파일 형식입니다."),
	FORBIDDEN_FILE_NAME_EXCEPTION(BAD_REQUEST, "허용되지 않은 파일 이름입니다."),
	FORBIDDEN_PARTICIPATE_COUNT_EXCEPTION(FORBIDDEN, "방 참가자는 16명을 초과할 수 없습니다."),
	FORBIDDEN_TODO_COUNT_EXCEPTION(FORBIDDEN, "todo 는 60개를 초과할 수 없습니다."),
	FORBIDDEN_RULE_COUNT_EXCEPTION(FORBIDDEN, "rule 은 30개를 초과할 수 없습니다."),
	FORBIDDEN_REPRESENT_RULE_COUNT_EXCEPTION(FORBIDDEN, "rule 은 3개를 초과할 수 없습니다."),
	FORBIDDEN_ROOM_PARTICIPATE_EXCEPTION(FORBIDDEN, "같은 방에 참가하고 있지 않습니다."),
	FORBIDDEN_ACQUIRE_BADGE_EXCEPTION(FORBIDDEN, "유저가 획득한 배지가 아닙니다."),

	/**
	 * 404 Not Found
	 */
	NOT_FOUND_EXCEPTION(NOT_FOUND, "존재하지 않습니다."),
	NOT_FOUND_OS_EXCEPTION(NOT_FOUND, "배포되지 않은 OS 입니다."),
	NOT_FOUND_USER_EXCEPTION(NOT_FOUND, "탈퇴했거나 존재하지 않는 유저입니다."),
	NOT_FOUND_REFRESH_TOKEN_EXCEPTION(NOT_FOUND, "만료된 리프레시 토큰입니다."),
	NOT_FOUND_ONBOARDING_EXCEPTION(NOT_FOUND, "유저의 온보딩 정보가 존재하지 않습니다."),
	NOT_FOUND_ROOM_EXCEPTION(NOT_FOUND, "존재하지 않는 방입니다."),
	NOT_FOUND_PARTICIPATE_EXCEPTION(NOT_FOUND, "참가중인 방이 존재하지 않습니다."),
	NOT_FOUND_RULE_EXCEPTION(NOT_FOUND, "존재하지 않는 규칙입니다."),
	NOT_FOUND_TODO_EXCEPTION(NOT_FOUND, "존재하지 않는 todo 입니다."),
	NOT_FOUND_BADGE_EXCEPTION(NOT_FOUND, "존재하지 않는 배지 입니다."),
	NOT_FOUND_PERSONALITY_COLOR_EXCEPTION(NOT_FOUND, "GRAY 에 대한 성향 정보는 존재하지 않습니다."),

	/**
	 * 405 Method Not Allowed
	 */
	METHOD_NOT_ALLOWED_EXCEPTION(METHOD_NOT_ALLOWED, "지원하지 않는 메소드 입니다."),

	/**
	 * 406 Not Acceptable
	 */
	NOT_ACCEPTABLE_EXCEPTION(NOT_ACCEPTABLE, "Not Acceptable"),

	/**
	 * 409 Conflict
	 */
	CONFLICT_EXCEPTION(CONFLICT, "이미 존재합니다."),
	CONFLICT_REQUEST_EXCEPTION(CONFLICT, "처리중인 요청입니다."),
	CONFLICT_USER_EXCEPTION(CONFLICT, "이미 해당 계정으로 회원가입하셨습니다.\n로그인 해주세요."),
	CONFLICT_LOGIN_EXCEPTION(CONFLICT, "이미 로그인 중인 유저입니다."),
	CONFLICT_JOINED_ROOM_EXCEPTION(CONFLICT, "이미 참가중인 방이 있습니다."),
	CONFLICT_RULE_EXCEPTION(CONFLICT, "이미 존재하는 규칙입니다."),
	CONFLICT_TODO_EXCEPTION(CONFLICT, "이미 존재하는 todo 입니다."),

	/**
	 * 415 Unsupported Media Type
	 */
	UNSUPPORTED_MEDIA_TYPE_EXCEPTION(UNSUPPORTED_MEDIA_TYPE, "해당하는 미디어 타입을 지원하지 않습니다."),

	/**
	 * 426 Upgrade Required
	 */
	UPGRADE_REQUIRED_EXCEPTION(UPGRADE_REQUIRED, "최신 버전으로 업그레이드가 필요합니다."),

	/**
	 * 500 Internal Server Exception
	 */
	INTERNAL_SERVER_EXCEPTION(INTERNAL_SERVER, "예상치 못한 서버 에러가 발생하였습니다."),

	/**
	 * 502 Bad Gateway
	 */
	BAD_GATEWAY_EXCEPTION(BAD_GATEWAY, "일시적인 에러가 발생하였습니다.\n잠시 후 다시 시도해주세요!"),

	/**
	 * 503 Service UnAvailable
	 */
	SERVICE_UNAVAILABLE_EXCEPTION(SERVICE_UNAVAILABLE, "현재 점검 중입니다.\n잠시 후 다시 시도해주세요!");

	private final ErrorStatusCode statusCode;
	private final String message;

	public int getStatus() {
		return statusCode.getStatus();
	}
}
