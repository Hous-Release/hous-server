package hous.server.controller.todo;

import hous.server.common.dto.ErrorResponse;
import hous.server.common.dto.SuccessResponse;
import hous.server.config.interceptor.Auth;
import hous.server.config.resolver.UserId;
import hous.server.service.todo.TodoService;
import hous.server.service.todo.dto.request.CheckTodoRequestDto;
import hous.server.service.todo.dto.request.TodoInfoRequestDto;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

@Api(tags = "Todo")
@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    @ApiOperation(
            value = "[인증] todo 추가 페이지 - 새로운 todo 를 생성합니다.",
            notes = "todo 이름을 15글자 이내로 설정하고 알림 여부, 담당자, 담당 요일을 설정하여 새로운 todo 생성을 요청합니다.\n" +
                    "성공시 status code = 204, 빈 response body를 보냅니다."
    )
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = ""),
            @ApiResponse(
                    code = 400,
                    message = "1. todo 이름을 입력해주세요. (name)\n"
                            + "2. todo 이름을 15 글자 이내로 입력해주세요. (name)\n"
                            + "3. 담당자 목록은 빈 배열일 수 없습니다. (todoUsers)\n"
                            + "4. 담당 요일 목록은 빈 배열일 수 없습니다. (todoUsers[0].dayOfWeeks)",
                    response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "토큰이 만료되었습니다. 다시 로그인 해주세요.", response = ErrorResponse.class),
            @ApiResponse(code = 403, message = "todo 는 60개를 초과할 수 없습니다.", response = ErrorResponse.class),
            @ApiResponse(
                    code = 404,
                    message = "1. 탈퇴했거나 존재하지 않는 유저입니다.\n"
                            + "2. 참가중인 방이 존재하지 않습니다.",
                    response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "예상치 못한 서버 에러가 발생하였습니다.", response = ErrorResponse.class)
    })
    @Auth
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("/todo")
    public ResponseEntity<String> createTodo(@Valid @RequestBody TodoInfoRequestDto request, @ApiIgnore @UserId Long userId) {
        todoService.createTodo(request, userId);
        return SuccessResponse.NO_CONTENT;
    }

    @ApiOperation(
            value = "[인증] todo 수정 페이지 - todo 를 수정합니다.",
            notes = "todo 이름을 15글자 이내로 설정하고 알림 여부, 담당자, 담당 요일을 설정하여 todo 수정을 요청합니다.\n" +
                    "성공시 status code = 204, 빈 response body를 보냅니다."
    )
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = ""),
            @ApiResponse(
                    code = 400,
                    message = "1. todo 이름을 입력해주세요. (name)\n"
                            + "2. todo 이름을 15 글자 이내로 입력해주세요. (name)\n"
                            + "3. 담당자 목록은 빈 배열일 수 없습니다. (todoUsers)\n"
                            + "4. 담당 요일 목록은 빈 배열일 수 없습니다. (todoUsers[0].dayOfWeeks)",
                    response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "토큰이 만료되었습니다. 다시 로그인 해주세요.", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "존재하지 않는 todo 입니다.", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "예상치 못한 서버 에러가 발생하였습니다.", response = ErrorResponse.class)
    })
    @Auth
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/todo/{todoId}")
    public ResponseEntity<String> updateTodo(@ApiParam(name = "todoId", value = "수정할 todo 의 id", required = true, example = "1")
                                             @PathVariable Long todoId,
                                             @Valid @RequestBody TodoInfoRequestDto request) {
        todoService.updateTodo(todoId, request);
        return SuccessResponse.NO_CONTENT;
    }

    @ApiOperation(
            value = "[인증] todo 메인 페이지 - todo 를 체크합니다.",
            notes = "체크 요청 (status = true), 해제 요청 (status = false) 로 todo 체크를 요청합니다.\n" +
                    "요청한 status 가 현재 서버의 status 인 경우, 400 에러 (잘못된 상태로 요청했습니다.) 를 전달합니다. \n" +
                    "성공시 status code = 204, 빈 response body를 보냅니다."
    )
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = ""),
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
            @ApiResponse(code = 500, message = "예상치 못한 서버 에러가 발생하였습니다.", response = ErrorResponse.class)
    })
    @Auth
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("/todo/{todoId}/check")
    public ResponseEntity<String> checkTodo(@ApiParam(name = "todoId", value = "체크할 todo 의 id", required = true, example = "1")
                                            @PathVariable Long todoId,
                                            @Valid @RequestBody CheckTodoRequestDto request,
                                            @ApiIgnore @UserId Long userId) {
        todoService.checkTodo(todoId, request, userId);
        return SuccessResponse.NO_CONTENT;
    }

    @ApiOperation(
            value = "[인증] todo 전체 페이지 - todo 를 삭제합니다.",
            notes = "todo 삭제를 요청합니다.\n" +
                    "성공시 status code = 204, 빈 response body를 보냅니다."
    )
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = ""),
            @ApiResponse(code = 401, message = "토큰이 만료되었습니다. 다시 로그인 해주세요.", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "존재하지 않는 todo 입니다.", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "예상치 못한 서버 에러가 발생하였습니다.", response = ErrorResponse.class)
    })
    @Auth
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/todo/{todoId}")
    public ResponseEntity<String> deleteTodo(@ApiParam(name = "todoId", value = "삭제할 todo 의 id", required = true, example = "1")
                                             @PathVariable Long todoId) {
        todoService.deleteTodo(todoId);
        return SuccessResponse.NO_CONTENT;
    }
}
