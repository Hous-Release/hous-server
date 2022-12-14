package hous.server.controller.todo;

import hous.server.common.dto.ErrorResponse;
import hous.server.common.dto.SuccessResponse;
import hous.server.common.success.SuccessCode;
import hous.server.config.interceptor.auth.Auth;
import hous.server.config.interceptor.version.Version;
import hous.server.config.resolver.UserId;
import hous.server.service.todo.TodoRetrieveService;
import hous.server.service.todo.dto.response.*;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@Api(tags = "Todo")
@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class TodoRetrieveController {

    private final TodoRetrieveService todoRetrieveService;

    @ApiOperation(
            value = "[인증] todo 추가 페이지 - 담당자 목록을 조회합니다.",
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
    @GetMapping("/todos")
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
    public ResponseEntity<SuccessResponse<TodoInfoResponse>> getTodoInfo(@ApiParam(name = "todoId", value = "조회할 todo 의 id", required = true, example = "1")
                                                                         @PathVariable Long todoId,
                                                                         @ApiIgnore @UserId Long userId) {
        return SuccessResponse.success(SuccessCode.GET_TODO_INFO_SUCCESS, todoRetrieveService.getTodoInfo(todoId, userId));
    }

    @ApiOperation(
            value = "[인증] todo 전체 보기 페이지 - 저장된 todo 요약 정보를 조회합니다.",
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
    public ResponseEntity<SuccessResponse<TodoSummaryInfoResponse>> getTodoSummaryInfo(@ApiParam(name = "todoId", value = "조회할 todo 의 id", required = true, example = "1")
                                                                                       @PathVariable Long todoId,
                                                                                       @ApiIgnore @UserId Long userId) {
        return SuccessResponse.success(SuccessCode.GET_TODO_SUMMARY_INFO_SUCCESS, todoRetrieveService.getTodoSummaryInfo(todoId, userId));
    }

    @ApiOperation(
            value = "[인증] todo 전체 보기 페이지 - 요일별 todo를 조회합니다.",
            notes = "모든 요일의 todo별 나의 todo와 방의 todo를 조회합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "todo 요일별 정보 조회 성공입니다."),
            @ApiResponse(code = 401, message = "토큰이 만료되었습니다. 다시 로그인 해주세요.", response = ErrorResponse.class),
            @ApiResponse(
                    code = 404,
                    message = "1. 탈퇴했거나 존재하지 않는 유저입니다.\n"
                            + "2. 존재하지 않는 todo 입니다.\n"
                            + "3. 참가중인 방이 존재하지 않습니다.",
                    response = ErrorResponse.class),
            @ApiResponse(code = 426, message = "최신 버전으로 업그레이드가 필요합니다.", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "예상치 못한 서버 에러가 발생하였습니다.", response = ErrorResponse.class)
    })
    @Version
    @Auth
    @GetMapping("/todos/day")
    public ResponseEntity<SuccessResponse<TodoAllDayResponse>> getTodoAllDayInfo(@ApiIgnore @UserId Long userId) {
        return SuccessResponse.success(SuccessCode.GET_TODO_ALL_DAY_SUCCESS, todoRetrieveService.getTodoAllDayInfo(userId));
    }

    @ApiOperation(
            value = "[인증] todo 전체 보기 페이지 - 멤버별 todo를 조회합니다.",
            notes = "방 내 모든 멤버의 요일별 todo를 조회합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "todo 멤버별 정보 조회 성공입니다."),
            @ApiResponse(code = 401, message = "토큰이 만료되었습니다. 다시 로그인 해주세요.", response = ErrorResponse.class),
            @ApiResponse(
                    code = 404,
                    message = "1. 탈퇴했거나 존재하지 않는 유저입니다.\n"
                            + "2. 존재하지 않는 todo 입니다.\n"
                            + "3. 참가중인 방이 존재하지 않습니다.",
                    response = ErrorResponse.class),
            @ApiResponse(code = 426, message = "최신 버전으로 업그레이드가 필요합니다.", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "예상치 못한 서버 에러가 발생하였습니다.", response = ErrorResponse.class)
    })
    @Version
    @Auth
    @GetMapping("/todos/member")
    public ResponseEntity<SuccessResponse<TodoAllMemberResponse>> getTodoAllMemberInfo(@ApiIgnore @UserId Long userId) {
        return SuccessResponse.success(SuccessCode.GET_TODO_ALL_MEMBER_SUCCESS, todoRetrieveService.getTodoAllMemberInfo(userId));
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
