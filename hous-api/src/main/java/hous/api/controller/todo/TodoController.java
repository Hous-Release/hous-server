package hous.api.controller.todo;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import hous.api.config.aop.duplicate.PreventDuplicateRequest;
import hous.api.config.interceptor.auth.Auth;
import hous.api.config.interceptor.version.Version;
import hous.api.config.resolver.UserId;
import hous.api.service.todo.TodoService;
import hous.api.service.todo.dto.request.CheckTodoRequestDto;
import hous.api.service.todo.dto.request.TodoInfoRequestDto;
import hous.common.dto.ErrorResponse;
import hous.common.dto.SuccessResponse;
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
public class TodoController {

	private final TodoService todoService;

	@ApiOperation(
		value = "[인증] todo 추가 페이지 - 새로운 todo 를 생성합니다.",
		notes = "todo 이름을 15글자 이내로 설정하고 알림 여부, 담당자, 담당 요일을 설정하여 새로운 todo 생성을 요청합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(code = 201, message = "생성 성공입니다."),
		@ApiResponse(
			code = 400,
			message = "1. todo 이름을 입력해주세요. (name)\n"
				+ "2. todo 이름을 15 글자 이내로 입력해주세요. (name)\n"
				+ "3. 담당자 목록은 빈 배열일 수 없습니다. (todoUsers)\n"
				+ "4. 담당 요일 목록은 빈 배열일 수 없습니다. (todoUsers[0].dayOfWeeks)\n"
				+ "5. todo 의 푸쉬 알림 여부를 입력해주세요. (isPushNotification)",
			response = ErrorResponse.class),
		@ApiResponse(code = 401, message = "토큰이 만료되었습니다. 다시 로그인 해주세요.", response = ErrorResponse.class),
		@ApiResponse(code = 403, message = "todo 는 60개를 초과할 수 없습니다.", response = ErrorResponse.class),
		@ApiResponse(
			code = 404,
			message = "1. 탈퇴했거나 존재하지 않는 유저입니다.\n"
				+ "2. 참가중인 방이 존재하지 않습니다.\n"
				+ "3. 유저의 온보딩 정보가 존재하지 않습니다.",
			response = ErrorResponse.class),
		@ApiResponse(
			code = 409,
			message = "1. 처리중인 요청입니다.\n"
				+ "2. 이미 존재하는 todo 입니다.",
			response = ErrorResponse.class),
		@ApiResponse(code = 426, message = "최신 버전으로 업그레이드가 필요합니다.", response = ErrorResponse.class),
		@ApiResponse(code = 500, message = "예상치 못한 서버 에러가 발생하였습니다.", response = ErrorResponse.class)
	})
	@PreventDuplicateRequest
	@Version
	@Auth
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/todo")
	public ResponseEntity<SuccessResponse<String>> createTodo(@ApiIgnore @UserId Long userId,
		@Valid @RequestBody TodoInfoRequestDto request) {
		todoService.createTodo(request, userId);
		return SuccessResponse.CREATED;
	}

	@ApiOperation(
		value = "[인증] todo 수정 페이지 - todo 를 수정합니다.",
		notes = "todo 이름을 15글자 이내로 설정하고 알림 여부, 담당자, 담당 요일을 설정하여 todo 수정을 요청합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "성공입니다."),
		@ApiResponse(
			code = 400,
			message = "1. todo 이름을 입력해주세요. (name)\n"
				+ "2. todo 이름을 15 글자 이내로 입력해주세요. (name)\n"
				+ "3. 담당자 목록은 빈 배열일 수 없습니다. (todoUsers)\n"
				+ "4. 담당 요일 목록은 빈 배열일 수 없습니다. (todoUsers[0].dayOfWeeks)\n"
				+ "5. todo 의 푸쉬 알림 여부를 입력해주세요. (isPushNotification)",
			response = ErrorResponse.class),
		@ApiResponse(code = 401, message = "토큰이 만료되었습니다. 다시 로그인 해주세요.", response = ErrorResponse.class),
		@ApiResponse(
			code = 404,
			message = "1. 탈퇴했거나 존재하지 않는 유저입니다.\n"
				+ "2. 참가중인 방이 존재하지 않습니다.\n"
				+ "3. 유저의 온보딩 정보가 존재하지 않습니다.\n"
				+ "4. 존재하지 않는 todo 입니다.",
			response = ErrorResponse.class),
		@ApiResponse(
			code = 409,
			message = "1. 처리중인 요청입니다.\n"
				+ "2. 이미 존재하는 todo 입니다.",
			response = ErrorResponse.class),
		@ApiResponse(code = 426, message = "최신 버전으로 업그레이드가 필요합니다.", response = ErrorResponse.class),
		@ApiResponse(code = 500, message = "예상치 못한 서버 에러가 발생하였습니다.", response = ErrorResponse.class)
	})
	@PreventDuplicateRequest
	@Version
	@Auth
	@PutMapping("/todo/{todoId}")
	public ResponseEntity<SuccessResponse<String>> updateTodo(@ApiIgnore @UserId Long userId,
		@ApiParam(name = "todoId", value = "수정할 todo 의 id", required = true, example = "1")
		@PathVariable Long todoId,
		@Valid @RequestBody TodoInfoRequestDto request) {
		todoService.updateTodo(todoId, request, userId);
		return SuccessResponse.OK;
	}

	@ApiOperation(
		value = "[인증] todo 메인 페이지 - todo 를 체크합니다.",
		notes = "체크 요청 (status = true), 해제 요청 (status = false) 로 todo 체크를 요청합니다.\n"
			+ "요청한 status 가 현재 서버의 status 인 경우, 400 에러 (잘못된 상태로 요청했습니다.) 를 전달합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "성공입니다."),
		@ApiResponse(
			code = 400,
			message = "1. 요청할 체크 상태를 입력해주세요.\n"
				+ "2. 잘못된 상태로 요청했습니다.",
			response = ErrorResponse.class),
		@ApiResponse(code = 401, message = "토큰이 만료되었습니다. 다시 로그인 해주세요.", response = ErrorResponse.class),
		@ApiResponse(
			code = 404,
			message = "1. 탈퇴했거나 존재하지 않는 유저입니다.\n"
				+ "2. 존재하지 않는 todo 입니다.",
			response = ErrorResponse.class),
		@ApiResponse(code = 409, message = "처리중인 요청입니다.", response = ErrorResponse.class),
		@ApiResponse(code = 426, message = "최신 버전으로 업그레이드가 필요합니다.", response = ErrorResponse.class),
		@ApiResponse(code = 500, message = "예상치 못한 서버 에러가 발생하였습니다.", response = ErrorResponse.class)
	})
	@PreventDuplicateRequest
	@Version
	@Auth
	@PostMapping("/todo/{todoId}/check")
	public ResponseEntity<SuccessResponse<String>> checkTodo(@ApiIgnore @UserId Long userId,
		@ApiParam(name = "todoId", value = "체크할 todo 의 id", required = true, example = "1")
		@PathVariable Long todoId,
		@Valid @RequestBody CheckTodoRequestDto request) {
		todoService.checkTodo(todoId, request, userId);
		return SuccessResponse.OK;
	}

	@ApiOperation(
		value = "[인증] todo 보기 페이지 - todo 를 삭제합니다.",
		notes = "todo 삭제를 요청합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "성공입니다."),
		@ApiResponse(code = 401, message = "토큰이 만료되었습니다. 다시 로그인 해주세요.", response = ErrorResponse.class),
		@ApiResponse(
			code = 404,
			message = "1. 탈퇴했거나 존재하지 않는 유저입니다.\n"
				+ "2. 존재하지 않는 todo 입니다.",
			response = ErrorResponse.class),
		@ApiResponse(code = 409, message = "처리중인 요청입니다.", response = ErrorResponse.class),
		@ApiResponse(code = 426, message = "최신 버전으로 업그레이드가 필요합니다.", response = ErrorResponse.class),
		@ApiResponse(code = 500, message = "예상치 못한 서버 에러가 발생하였습니다.", response = ErrorResponse.class)
	})
	@PreventDuplicateRequest
	@Version
	@Auth
	@DeleteMapping("/todo/{todoId}")
	public ResponseEntity<SuccessResponse<String>> deleteTodo(@ApiIgnore @UserId Long userId,
		@ApiParam(name = "todoId", value = "삭제할 todo 의 id", required = true, example = "1")
		@PathVariable Long todoId) {
		todoService.deleteTodo(todoId, userId);
		return SuccessResponse.OK;
	}
}
