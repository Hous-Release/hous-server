package hous.server.controller.todo;

import hous.server.common.dto.ErrorResponse;
import hous.server.common.dto.SuccessResponse;
import hous.server.common.success.SuccessCode;
import hous.server.config.interceptor.Auth;
import hous.server.config.resolver.UserId;
import hous.server.service.todo.TodoRetrieveService;
import hous.server.service.todo.dto.response.GetUsersInfoResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
            @ApiResponse(code = 500, message = "예상치 못한 서버 에러가 발생하였습니다.", response = ErrorResponse.class)
    })
    @Auth
    @GetMapping("/todo")
    public ResponseEntity<GetUsersInfoResponse> getUsersInfo(@ApiIgnore @UserId Long userId) {
        return SuccessResponse.success(SuccessCode.GET_USERS_INFO_SUCCESS, todoRetrieveService.getUsersInfo(userId));
    }
}
