package hous.api.controller.room;

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
import hous.api.service.room.RoomService;
import hous.api.service.room.dto.request.SetRoomNameRequestDto;
import hous.api.service.room.dto.response.RoomInfoResponse;
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

@Api(tags = "Room")
@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class RoomController {

	private final RoomService roomService;

	@ApiOperation(
		value = "[인증] 방 만들기/입장 페이지 - 방을 만듭니다.",
		notes = "방 이름을 8글자 이내로 설정하고 방 생성을 요청합니다.\n"
			+ "방이 생성되면 방의 id 와 초대코드를 전달합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(code = 201, message = "방 생성 성공입니다."),
		@ApiResponse(
			code = 400,
			message = "1. 방 이름을 입력해주세요. (name)\n"
				+ "2. 방 이름을 8 글자 이내로 입력해주세요. (name)",
			response = ErrorResponse.class),
		@ApiResponse(code = 401, message = "토큰이 만료되었습니다. 다시 로그인 해주세요.", response = ErrorResponse.class),
		@ApiResponse(code = 404, message = "탈퇴했거나 존재하지 않는 유저입니다.", response = ErrorResponse.class),
		@ApiResponse(
			code = 409,
			message = "1. 처리중인 요청입니다.\n"
				+ "2. 이미 참가중인 방이 있습니다.",
			response = ErrorResponse.class),
		@ApiResponse(code = 426, message = "최신 버전으로 업그레이드가 필요합니다.", response = ErrorResponse.class),
		@ApiResponse(code = 500, message = "예상치 못한 서버 에러가 발생하였습니다.", response = ErrorResponse.class)
	})
	@PreventDuplicateRequest
	@Version
	@Auth
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/room")
	public ResponseEntity<SuccessResponse<RoomInfoResponse>> createRoom(@ApiIgnore @UserId Long userId,
		@Valid @RequestBody SetRoomNameRequestDto request) {
		return SuccessResponse.success(SuccessCode.CREATE_ROOM_SUCCESS, roomService.createRoom(request, userId));
	}

	@ApiOperation(
		value = "[인증] 방 만들기/입장 페이지 - 방에 참여합니다.",
		notes = "방 코드 입력 후 페이지에서 참여하기 버튼을 누를 때 요청합니다.\n"
			+ "방 초대코드로 조회한 방의 id 로 입장을 요청합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "방 참여 성공입니다."),
		@ApiResponse(code = 401, message = "토큰이 만료되었습니다. 다시 로그인 해주세요.", response = ErrorResponse.class),
		@ApiResponse(code = 403, message = "방 참가자는 16명을 초과할 수 없습니다.", response = ErrorResponse.class),
		@ApiResponse(
			code = 404,
			message = "1. 탈퇴했거나 존재하지 않는 유저입니다.\n"
				+ "2. 존재하지 않는 방입니다.",
			response = ErrorResponse.class),
		@ApiResponse(
			code = 409,
			message = "1. 처리중인 요청입니다.\n"
				+ "2. 이미 참가중인 방이 있습니다.",
			response = ErrorResponse.class),
		@ApiResponse(code = 426, message = "최신 버전으로 업그레이드가 필요합니다.", response = ErrorResponse.class),
		@ApiResponse(code = 500, message = "예상치 못한 서버 에러가 발생하였습니다.", response = ErrorResponse.class)
	})
	@PreventDuplicateRequest
	@Version
	@Auth
	@PostMapping("/room/{roomId}/join")
	public ResponseEntity<SuccessResponse<RoomInfoResponse>> joinRoom(@ApiIgnore @UserId Long userId,
		@ApiParam(name = "roomId", value = "참가할 room 의 id", required = true, example = "1")
		@PathVariable Long roomId) {
		return SuccessResponse.success(SuccessCode.JOIN_ROOM_SUCCESS, roomService.joinRoom(roomId, userId));
	}

	@ApiOperation(
		value = "[인증] Hous- 페이지 - 방 별명을 수정합니다.",
		notes = "방 별명을 8글자 이내로 설정하여 수정을 요청합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "성공입니다."),
		@ApiResponse(
			code = 400,
			message = "1. 방 이름을 입력해주세요. (name)\n"
				+ "2. 방 이름을 8 글자 이내로 입력해주세요. (name)",
			response = ErrorResponse.class),
		@ApiResponse(code = 401, message = "토큰이 만료되었습니다. 다시 로그인 해주세요.", response = ErrorResponse.class),
		@ApiResponse(
			code = 404,
			message = "1. 탈퇴했거나 존재하지 않는 유저입니다.\n"
				+ "2. 참가중인 방이 존재하지 않습니다.",
			response = ErrorResponse.class),
		@ApiResponse(code = 409, message = "처리중인 요청입니다.", response = ErrorResponse.class),
		@ApiResponse(code = 426, message = "최신 버전으로 업그레이드가 필요합니다.", response = ErrorResponse.class),
		@ApiResponse(code = 500, message = "예상치 못한 서버 에러가 발생하였습니다.", response = ErrorResponse.class)
	})
	@PreventDuplicateRequest
	@Version
	@Auth
	@PutMapping("/room/name")
	public ResponseEntity<SuccessResponse<String>> updateRoomName(@ApiIgnore @UserId Long userId,
		@Valid @RequestBody SetRoomNameRequestDto request) {
		roomService.updateRoomName(request, userId);
		return SuccessResponse.OK;
	}

	@ApiOperation(
		value = "[인증] 마이 페이지(설정) - 방에서 퇴사합니다.",
		notes = "퇴사 성공시 방 생성, 입장 페이지로 이동합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "성공입니다."),
		@ApiResponse(code = 401, message = "토큰이 만료되었습니다. 다시 로그인 해주세요.", response = ErrorResponse.class),
		@ApiResponse(
			code = 404,
			message = "1. 탈퇴했거나 존재하지 않는 유저입니다.\n"
				+ "2. 참가중인 방이 존재하지 않습니다.",
			response = ErrorResponse.class),
		@ApiResponse(code = 409, message = "처리중인 요청입니다.", response = ErrorResponse.class),
		@ApiResponse(code = 426, message = "최신 버전으로 업그레이드가 필요합니다.", response = ErrorResponse.class),
		@ApiResponse(code = 500, message = "예상치 못한 서버 에러가 발생하였습니다.", response = ErrorResponse.class)
	})
	@PreventDuplicateRequest
	@Version
	@Auth
	@DeleteMapping("/room/leave")
	public ResponseEntity<SuccessResponse<String>> leaveRoom(@ApiIgnore @UserId Long userId) {
		roomService.leaveRoom(userId);
		return SuccessResponse.OK;
	}
}
