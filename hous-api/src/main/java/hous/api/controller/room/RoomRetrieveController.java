package hous.api.controller.room;

import hous.api.config.interceptor.auth.Auth;
import hous.api.config.interceptor.version.Version;
import hous.api.config.resolver.UserId;
import hous.api.service.room.RoomRetrieveService;
import hous.api.service.room.dto.response.GetRoomInfoResponse;
import hous.api.service.room.dto.response.GetRoomResponse;
import hous.common.dto.ErrorResponse;
import hous.common.dto.SuccessResponse;
import hous.common.success.SuccessCode;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@Api(tags = "Room")
@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
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
            @ApiResponse(code = 426, message = "최신 버전으로 업그레이드가 필요합니다.", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "예상치 못한 서버 에러가 발생하였습니다.", response = ErrorResponse.class)
    })
    @Version
    @Auth
    @GetMapping("/room")
    public ResponseEntity<SuccessResponse<GetRoomResponse>> getRoom(@ApiIgnore @UserId Long userId) {
        return SuccessResponse.success(SuccessCode.GET_ROOM_SUCCESS, roomRetrieveService.getRoom(userId));
    }

    @ApiOperation(
            value = "[인증] 방 만들기/입장 페이지 - 참가하려는 방 정보를 조회합니다.",
            notes = "방 코드와 일치하는 방이 존재한다면 방의 id 와 방장의 닉네임을 전달합니다.\n" +
                    "방 코드와 일치하는 방이 없다면 null 을 전달합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "참가하려는 방 정보 조회 성공입니다."),
            @ApiResponse(code = 401, message = "토큰이 만료되었습니다. 다시 로그인 해주세요.", response = ErrorResponse.class),
            @ApiResponse(code = 426, message = "최신 버전으로 업그레이드가 필요합니다.", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "예상치 못한 서버 에러가 발생하였습니다.", response = ErrorResponse.class)
    })
    @Version
    @Auth
    @GetMapping("/room/info")
    public ResponseEntity<SuccessResponse<GetRoomInfoResponse>> getRoomInfo(@ApiParam(name = "code", value = "참가하려는 방 코드", required = true, example = "PNO6VN6A")
                                                                            @RequestParam String code) {
        return SuccessResponse.success(SuccessCode.GET_ROOM_INFO_SUCCESS, roomRetrieveService.getRoomInfo(code));
    }
}
