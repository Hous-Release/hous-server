package hous.server.controller.room;

import hous.server.common.dto.ErrorResponse;
import hous.server.common.dto.SuccessResponse;
import hous.server.common.success.SuccessCode;
import hous.server.config.interceptor.Auth;
import hous.server.config.resolver.UserId;
import hous.server.service.room.RoomRetrieveService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@Api(tags = "Room")
@RequiredArgsConstructor
@RestController
public class RoomRetrieveController {

    private final RoomRetrieveService roomRetrieveService;

    @ApiOperation(
            value = "[인증] 방 만들기/입장 페이지 - 참가중인 방을 조회합니다.",
            notes = "참가중인 방이 있다면 방의 id 를 전달합니다.\n" +
                    "참가중인 방이 없다면 null 을 전달합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "참가중인 방 조회 성공입니다."),
            @ApiResponse(code = 401, message = "토큰이 만료되었습니다. 다시 로그인 해주세요.", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "탈퇴했거나 존재하지 않는 유저입니다.", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "예상치 못한 서버 에러가 발생하였습니다.", response = ErrorResponse.class)
    })
    @Auth
    @GetMapping("/v1/room")
    public ResponseEntity<SuccessResponse> getRoom(@ApiIgnore @UserId Long userId) {
        return SuccessResponse.success(SuccessCode.GET_ROOM_SUCCESS, roomRetrieveService.getRoom(userId));
    }
}
