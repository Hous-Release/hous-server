package hous.api.controller.todo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hous.api.config.interceptor.auth.Auth;
import hous.api.config.interceptor.version.Version;
import hous.api.config.resolver.UserId;
import hous.api.service.todo.TodoRetrieveService;
import hous.api.service.todo.dto.response.MyTodoInfoResponse;
import hous.api.service.todo.dto.response.TodoAddableResponse;
import hous.api.service.todo.dto.response.TodoFilterResponse;
import hous.api.service.todo.dto.response.TodoInfoResponse;
import hous.api.service.todo.dto.response.TodoMainResponse;
import hous.api.service.todo.dto.response.TodoSummaryInfoResponse;
import hous.api.service.todo.dto.response.UserPersonalityInfoResponse;
import hous.common.dto.ErrorResponse;
import hous.common.dto.SuccessResponse;
import hous.common.success.SuccessCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import springfox.documentation.annotations.ApiIgnore;

@Api(tags = "Todo")
@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class TodoRetrieveController {

	private final TodoRetrieveService todoRetrieveService;

	@ApiOperation(
		value = "[인증] todo 메인 페이지 - todo 추가 가능 여부를 조회합니다.",
		notes = "todo 개수가 60개 미만인 경우 true, 60개 이상인 경우 false 를 전달합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "todo 추가 가능 여부 조회 성공입니다."),
		@ApiResponse(code = 401, message = "토큰이 만료되었습니다. 다시 로그인 해주세요.", response = ErrorResponse.class),
		@ApiResponse(
			code = 404,
			message = "1. 탈퇴했거나 존재하지 않는 유저입니다.\n"
				+ "2. 참가중인 방이 존재하지 않습니다.",
			response = ErrorResponse.class),
		@ApiResponse(code = 426, message = "최신 버전으로 업그레이드가 필요합니다.", response = ErrorResponse.class),
		@ApiResponse(code = 500, message = "예상치 못한 서버 에러가 발생하였습니다.", response = ErrorResponse.class)
	})
	@Version
	@Auth
	@GetMapping("/todo/addable")
	public ResponseEntity<SuccessResponse<TodoAddableResponse>> getTodoAddable(@ApiIgnore @UserId Long userId) {
		return SuccessResponse.success(SuccessCode.GET_TODO_ADDABLE_SUCCESS,
			todoRetrieveService.getTodoAddable(userId));
	}

	@ApiOperation(
		value = "[인증] todo 추가 페이지 / todo 필터 바텀 시트 - 담당자 목록을 조회합니다.",
		notes = "방에 참가중인 사용자들의 id, 성향 색, 닉네임을 조회합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "담당자 목록 조회 성공입니다."),
		@ApiResponse(code = 401, message = "토큰이 만료되었습니다. 다시 로그인 해주세요.", response = ErrorResponse.class),
		@ApiResponse(
			code = 404,
			message = "1. 탈퇴했거나 존재하지 않는 유저입니다.\n"
				+ "2. 참가중인 방이 존재하지 않습니다.",
			response = ErrorResponse.class),
		@ApiResponse(code = 426, message = "최신 버전으로 업그레이드가 필요합니다.", response = ErrorResponse.class),
		@ApiResponse(code = 500, message = "예상치 못한 서버 에러가 발생하였습니다.", response = ErrorResponse.class)
	})
	@Version
	@Auth
	@GetMapping("/todo")
	public ResponseEntity<SuccessResponse<UserPersonalityInfoResponse>> getUsersInfo(@ApiIgnore @UserId Long userId) {
		return SuccessResponse.success(SuccessCode.GET_USERS_INFO_SUCCESS, todoRetrieveService.getUsersInfo(userId));
	}

	@ApiOperation(
		value = "[인증] todo 메인 페이지 - todo 메인 페이지에 필요한 정보를 조회합니다.",
		notes = "오늘 날짜, todo 진행률, my todo, our todo 를 조회합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "todo 메인 페이지 조회 성공입니다."),
		@ApiResponse(code = 401, message = "토큰이 만료되었습니다. 다시 로그인 해주세요.", response = ErrorResponse.class),
		@ApiResponse(
			code = 404,
			message = "1. 탈퇴했거나 존재하지 않는 유저입니다.\n"
				+ "2. 참가중인 방이 존재하지 않습니다.",
			response = ErrorResponse.class),
		@ApiResponse(code = 426, message = "최신 버전으로 업그레이드가 필요합니다.", response = ErrorResponse.class),
		@ApiResponse(code = 500, message = "예상치 못한 서버 에러가 발생하였습니다.", response = ErrorResponse.class)
	})
	@Version
	@Auth
	@GetMapping("/todos/main")
	public ResponseEntity<SuccessResponse<TodoMainResponse>> getTodoMain(@ApiIgnore @UserId Long userId) {
		return SuccessResponse.success(SuccessCode.GET_TODO_MAIN_SUCCESS, todoRetrieveService.getTodoMain(userId));
	}

	@ApiOperation(
		value = "[인증] todo 수정 페이지 - 저장된 todo 정보를 조회합니다.",
		notes = "저장된 todo 이름, 알림 여부, 담당자, 담당 요일을 조회합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "todo 정보 조회 성공입니다."),
		@ApiResponse(code = 401, message = "토큰이 만료되었습니다. 다시 로그인 해주세요.", response = ErrorResponse.class),
		@ApiResponse(
			code = 404,
			message = "1. 탈퇴했거나 존재하지 않는 유저입니다.\n"
				+ "2. 참가중인 방이 존재하지 않습니다.\n"
				+ "3. 존재하지 않는 todo 입니다.",
			response = ErrorResponse.class),
		@ApiResponse(code = 426, message = "최신 버전으로 업그레이드가 필요합니다.", response = ErrorResponse.class),
		@ApiResponse(code = 500, message = "예상치 못한 서버 에러가 발생하였습니다.", response = ErrorResponse.class)
	})
	@Version
	@Auth
	@GetMapping("/todo/{todoId}")
	public ResponseEntity<SuccessResponse<TodoInfoResponse>> getTodoInfo(
		@ApiParam(name = "todoId", value = "조회할 todo 의 id", required = true, example = "1")
		@PathVariable Long todoId,
		@ApiIgnore @UserId Long userId) {
		return SuccessResponse.success(SuccessCode.GET_TODO_INFO_SUCCESS,
			todoRetrieveService.getTodoInfo(todoId, userId));
	}

	@ApiOperation(
		value = "[인증] todo 보기 페이지 - 저장된 todo 요약 정보를 조회합니다.",
		notes = "저장된 전체 담당자, 전체 담당 요일을 조회합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "todo 요약 정보 조회 성공입니다."),
		@ApiResponse(code = 401, message = "토큰이 만료되었습니다. 다시 로그인 해주세요.", response = ErrorResponse.class),
		@ApiResponse(
			code = 404,
			message = "1. 탈퇴했거나 존재하지 않는 유저입니다.\n"
				+ "2. 존재하지 않는 todo 입니다.",
			response = ErrorResponse.class),
		@ApiResponse(code = 426, message = "최신 버전으로 업그레이드가 필요합니다.", response = ErrorResponse.class),
		@ApiResponse(code = 500, message = "예상치 못한 서버 에러가 발생하였습니다.", response = ErrorResponse.class)
	})
	@Version
	@Auth
	@GetMapping("/todo/{todoId}/summary")
	public ResponseEntity<SuccessResponse<TodoSummaryInfoResponse>> getTodoSummaryInfo(
		@ApiParam(name = "todoId", value = "조회할 todo 의 id", required = true, example = "1")
		@PathVariable Long todoId,
		@ApiIgnore @UserId Long userId) {
		return SuccessResponse.success(SuccessCode.GET_TODO_SUMMARY_INFO_SUCCESS,
			todoRetrieveService.getTodoSummaryInfo(todoId, userId));
	}

	@ApiOperation(
		value = "[인증] todo 보기 페이지 - 필터별 todo 를 조회합니다.",
		notes = "필터를 적용하여 todo 를 조회합니다.\n"
			+ "dayOfWeeks 에는 MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY 를 , 로 구별하여 설정해주세요.\n"
			+ "onboardingIds 에는 호미 id 를 , 로 구별하여 설정해주세요.\n"
			+ "쉼표 사이에는 띄어쓰기를 포함하지 말아주세요.\n"
			+ "필터가 적용되지 않는 경우는 각각 null 을 보내주세요."
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "필터별 todo 조회 성공입니다."),
		@ApiResponse(
			code = 400,
			message = "1. 잘못된 dayOfWeeks 형태입니다.\n"
				+ "2. 잘못된 onboardingIds 형태입니다.",
			response = ErrorResponse.class),
		@ApiResponse(code = 401, message = "토큰이 만료되었습니다. 다시 로그인 해주세요.", response = ErrorResponse.class),
		@ApiResponse(
			code = 404,
			message = "1. 탈퇴했거나 존재하지 않는 유저입니다.\n"
				+ "2. 참가중인 방이 존재하지 않습니다.",
			response = ErrorResponse.class),
		@ApiResponse(code = 426, message = "최신 버전으로 업그레이드가 필요합니다.", response = ErrorResponse.class),
		@ApiResponse(code = 500, message = "예상치 못한 서버 에러가 발생하였습니다.", response = ErrorResponse.class)
	})
	@Version
	@Auth
	@GetMapping("/todos")
	public ResponseEntity<SuccessResponse<TodoFilterResponse>> getTodosByFilter(
		@ApiParam(name = "dayOfWeeks", value = "필터로 적용할 요일 목록", example = "MONDAY,TUESDAY")
		@RequestParam(required = false) String dayOfWeeks,
		@ApiParam(name = "onboardingIds", value = "필터로 적용할 호미 id 목록", example = "1,2,3")
		@RequestParam(required = false) String onboardingIds,
		@ApiIgnore @UserId Long userId) {
		return SuccessResponse.success(SuccessCode.GET_TODO_BY_FILTER_SUCCESS,
			todoRetrieveService.getTodosByFilter(dayOfWeeks, onboardingIds, userId));
	}

	@ApiOperation(
		value = "[인증] 마이 페이지(설정) - MY to-do 를 조회합니다.",
		notes = "방에서 퇴사 요청 시 방에 참여하고 있을 때 담당자가 '나'인 to-do 를 조회합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "나의 todo 정보 조회 성공입니다."),
		@ApiResponse(code = 401, message = "토큰이 만료되었습니다. 다시 로그인 해주세요.", response = ErrorResponse.class),
		@ApiResponse(
			code = 404,
			message = "1. 탈퇴했거나 존재하지 않는 유저입니다.\n"
				+ "2. 참가중인 방이 존재하지 않습니다.",
			response = ErrorResponse.class),
		@ApiResponse(code = 426, message = "최신 버전으로 업그레이드가 필요합니다.", response = ErrorResponse.class),
		@ApiResponse(code = 500, message = "예상치 못한 서버 에러가 발생하였습니다.", response = ErrorResponse.class)
	})
	@Version
	@Auth
	@GetMapping("/todos/me")
	public ResponseEntity<SuccessResponse<MyTodoInfoResponse>> getMyTodoInfo(@ApiIgnore @UserId Long userId) {
		return SuccessResponse.success(SuccessCode.GET_MY_TODO_SUCCESS, todoRetrieveService.getMyTodoInfo(userId));
	}
}
