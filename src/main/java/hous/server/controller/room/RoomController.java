package hous.server.controller.room;

import hous.server.common.dto.ErrorResponse;
import hous.server.common.dto.SuccessResponse;
import hous.server.common.success.SuccessCode;
import hous.server.config.interceptor.Auth;
import hous.server.config.resolver.UserId;
import hous.server.service.room.RoomService;
import hous.server.service.room.dto.request.CreateRoomRequestDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

@Api(tags = "Room")
@RestController
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @ApiOperation(
            value = "방 만들기/입장 페이지 - 방을 만듭니다.",
            notes = "방 이름을 8글자 이내로 설정하고 방 생성을 요청합니다.\n" +
                    "방이 생성되면 방의 id 와 초대코드를 전달합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "방 생성 성공입니다."),
            @ApiResponse(
                    code = 400,
                    message = "1. 방 이름을 입력해주세요.\n"
                            + "2. 방 이름을 8 글자 이내로 입력해주세요.",
                    response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "토큰이 만료되었습니다. 다시 로그인 해주세요.", response = ErrorResponse.class),
            @ApiResponse(code = 409, message = "이미 참가중인 방이 있습니다.", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "예상치 못한 서버 에러가 발생하였습니다.", response = ErrorResponse.class)
    })
    @Auth
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/v1/room")
    public ResponseEntity<SuccessResponse> createRoom(@Valid @RequestBody CreateRoomRequestDto request, @ApiIgnore @UserId Long userId) {
        return SuccessResponse.success(SuccessCode.CREATE_ROOM_SUCCESS, roomService.createRoom(request, userId));
    }
}
