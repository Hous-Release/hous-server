package hous.api.controller.user;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import hous.api.config.aop.duplicate.PreventDuplicateRequest;
import hous.api.config.interceptor.auth.Auth;
import hous.api.config.interceptor.version.Version;
import hous.api.config.resolver.UserId;
import hous.api.config.sqs.producer.SqsProducer;
import hous.api.service.user.UserRetrieveService;
import hous.api.service.user.UserService;
import hous.api.service.user.dto.request.DeleteUserRequestDto;
import hous.api.service.user.dto.request.UpdatePushSettingRequestDto;
import hous.api.service.user.dto.request.UpdateTestScoreRequestDto;
import hous.api.service.user.dto.request.UpdateUserInfoRequestDto;
import hous.api.service.user.dto.request.UserFeedbackRequestDto;
import hous.api.service.user.dto.response.UpdatePersonalityColorResponse;
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

@Api(tags = "User")
@RequiredArgsConstructor
@RestController
public class UserController {

	private final UserService userService;
	private final UserRetrieveService userRetrieveService;
	private final SqsProducer sqsProducer;

	@ApiOperation(
		value = "[인증] 마이 페이지(Profile 뷰) - 나의 프로필 정보를 수정합니다.",
		notes = "프로필 정보 수정을 요청합니다. 자기소개에서 줄바꿈을 포함할 경우, ' '(공백)으로 변환하여 저장합니다.\n"
			+ "** iOS의 경우 생년월일을 아무것도 입력하지 않은 경우 -> birthday(\"\"), isPublic(false)로 부탁드립니다."
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "성공입니다."),
		@ApiResponse(
			code = 400,
			message = "1. 닉네임을 입력해주세요. (nickname)\n"
				+ "2. 닉네임은 최대 3글자까지 가능합니다. (nickname)\n"
				+ "3. 생년월일을 입력해주세요. (birthday)\n"
				+ "4. 생년월일을 공개 여부를 체크해주세요. (isPublic)\n"
				+ "5. mbti 는 4 글자 이내로 입력해주세요. (mbti)\n"
				+ "6. 직업은 3 글자 이내로 입력해주세요. (job)\n"
				+ "7. 자기소개는 40 글자 이내로 입력해주세요. (introduction)\n"
				+ "8. 생년월일이 없는 경우 공개 여부는 true가 될 수 없습니다.",
			response = ErrorResponse.class),
		@ApiResponse(code = 401, message = "토큰이 만료되었습니다. 다시 로그인 해주세요.", response = ErrorResponse.class),
		@ApiResponse(code = 404,
			message = "1. 탈퇴했거나 존재하지 않는 유저입니다. \n"
				+ "2. 존재하지 않는 방입니다.",
			response = ErrorResponse.class),
		@ApiResponse(code = 409, message = "처리중인 요청입니다.", response = ErrorResponse.class),
		@ApiResponse(code = 426, message = "최신 버전으로 업그레이드가 필요합니다.", response = ErrorResponse.class),
		@ApiResponse(code = 500, message = "예상치 못한 서버 에러가 발생하였습니다.", response = ErrorResponse.class)
	})
	@PreventDuplicateRequest
	@Version
	@Auth
	@PutMapping("/v1/user")
	public ResponseEntity<SuccessResponse<String>> updateUserInfo(@ApiIgnore @UserId Long userId,
		@Valid @RequestBody UpdateUserInfoRequestDto request) {
		userService.updateUserInfo(request, userId);
		return SuccessResponse.OK;
	}

	@ApiOperation(
		value = "[인증] 마이 페이지(설정) - 나의 푸시 알림 설정 정보를 수정합니다.",
		notes = "푸시 알림 설정 여부를 설정합니다.\n"
			+ "6가지 상태값 중 수정할 상태값 1개만 담아서 요청합니다.\n"
			+ "Rules, Badge 설정으로는 ON, OFF 를 담습니다.\n"
			+ "Todo 관련 설정으로는 ON_ALL, ON_MY, OFF 를 담습니다."
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "성공입니다."),
		@ApiResponse(code = 400, message = "잘못된 상태로 요청했습니다.", response = ErrorResponse.class),
		@ApiResponse(code = 401, message = "토큰이 만료되었습니다. 다시 로그인 해주세요.", response = ErrorResponse.class),
		@ApiResponse(code = 404, message = "탈퇴했거나 존재하지 않는 유저입니다.", response = ErrorResponse.class),
		@ApiResponse(code = 409, message = "처리중인 요청입니다.", response = ErrorResponse.class),
		@ApiResponse(code = 426, message = "최신 버전으로 업그레이드가 필요합니다.", response = ErrorResponse.class),
		@ApiResponse(code = 500, message = "예상치 못한 서버 에러가 발생하였습니다.", response = ErrorResponse.class)
	})
	@PreventDuplicateRequest
	@Version
	@Auth
	@PatchMapping("/v1/user/push")
	public ResponseEntity<SuccessResponse<String>> updateUserPushSetting(@ApiIgnore @UserId Long userId,
		@Valid @RequestBody UpdatePushSettingRequestDto request) {
		userService.updateUserPushSetting(request, userId);
		return SuccessResponse.OK;
	}

	@ApiOperation(
		value = "[인증] 마이 페이지(Profile 뷰) - 성향테스트 결과 정보를 수정합니다.",
		notes = "성향테스트 결과를 수정 후 변경된 성향테스트 색상을 전달합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "성향테스트 수정 성공입니다."),
		@ApiResponse(
			code = 400, message = "성향 테스트의 각 성향 점수는 최소 3점, 최대 9점입니다. (smell, light, noise, clean, introversion)",
			response = ErrorResponse.class),
		@ApiResponse(code = 401, message = "토큰이 만료되었습니다. 다시 로그인 해주세요.", response = ErrorResponse.class),
		@ApiResponse(code = 404,
			message = "1. 탈퇴했거나 존재하지 않는 유저입니다. \n"
				+ "2. 같은 방에 참가하고 있지 않습니다.",
			response = ErrorResponse.class),
		@ApiResponse(code = 409, message = "처리중인 요청입니다.", response = ErrorResponse.class),
		@ApiResponse(code = 426, message = "최신 버전으로 업그레이드가 필요합니다.", response = ErrorResponse.class),
		@ApiResponse(code = 500, message = "예상치 못한 서버 에러가 발생하였습니다.", response = ErrorResponse.class)
	})
	@PreventDuplicateRequest
	@Version
	@Auth
	@PutMapping("/v1/user/personality")
	public ResponseEntity<SuccessResponse<UpdatePersonalityColorResponse>> updateUserTestScore(
		@ApiIgnore @UserId Long userId,
		@Valid @RequestBody UpdateTestScoreRequestDto request) {
		return SuccessResponse.success(SuccessCode.UPDATE_PERSONALITY_TEST_SUCCESS,
			userService.updateUserTestScore(request, userId));
	}

	@ApiOperation(
		value = "[인증] 마이 페이지(배지 목록 뷰) - 대표 배지를 설정합니다.",
		notes = "대표 배지를 설정합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "성공입니다."),
		@ApiResponse(code = 401, message = "토큰이 만료되었습니다. 다시 로그인 해주세요.", response = ErrorResponse.class),
		@ApiResponse(code = 403, message = "유저가 획득한 배지가 아닙니다.", response = ErrorResponse.class),
		@ApiResponse(code = 404,
			message = "1. 탈퇴했거나 존재하지 않는 유저입니다. \n"
				+ "2. 참가중인 방이 존재하지 않습니다. \n"
				+ "3. 존재하지 않는 배지 입니다.",
			response = ErrorResponse.class),
		@ApiResponse(code = 409, message = "처리중인 요청입니다.", response = ErrorResponse.class),
		@ApiResponse(code = 426, message = "최신 버전으로 업그레이드가 필요합니다.", response = ErrorResponse.class),
		@ApiResponse(code = 500, message = "예상치 못한 서버 에러가 발생하였습니다.", response = ErrorResponse.class)
	})
	@PreventDuplicateRequest
	@Version
	@Auth
	@PutMapping("/v1/user/badge/{badgeId}/represent")
	public ResponseEntity<SuccessResponse<String>> updateRepresentBadge(@ApiIgnore @UserId Long userId,
		@ApiParam(name = "badgeId", value = "대표 배지로 설정할 badge 의 id", required = true, example = "1")
		@PathVariable Long badgeId) {
		userService.updateRepresentBadge(badgeId, userId);
		return SuccessResponse.OK;
	}

	// TODO: 2023/08/01 Deprecated
	@ApiOperation(
		value = "@@ Deprecated 될 API 입니다. @@ [인증] 마이 페이지(설정) - 회원 정보를 삭제합니다.",
		notes = "회원 정보 탈퇴 요청 시 해당 유저의 모든 정보를 삭제합니다.\n"
			+ "feedbackType을 NO를 보낸 경우, 사유가 없는 것으로 판단합니다. comment가 없는 경우 빈스트링(\"\")으로 보내주세요."
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "성공입니다."),
		@ApiResponse(code = 400,
			message = "1. 사유를 선택 안한 경우, NO를 보내주세요. (feedbackType)\n"
				+ "2. 의견은 200 글자 이내로 입력해주세요. (comment)\n"
				+ "3. 의견이 없는 경우, 빈 스트링(\"\")을 보내주세요. (comment)",
			response = ErrorResponse.class),
		@ApiResponse(code = 401, message = "토큰이 만료되었습니다. 다시 로그인 해주세요.", response = ErrorResponse.class),
		@ApiResponse(code = 404, message = "탈퇴했거나 존재하지 않는 유저입니다.", response = ErrorResponse.class),
		@ApiResponse(code = 409, message = "처리중인 요청입니다.", response = ErrorResponse.class),
		@ApiResponse(code = 426, message = "최신 버전으로 업그레이드가 필요합니다.", response = ErrorResponse.class),
		@ApiResponse(code = 500, message = "예상치 못한 서버 에러가 발생하였습니다.", response = ErrorResponse.class)
	})
	@PreventDuplicateRequest
	@Version
	@Auth
	@DeleteMapping("/v1/user")
	public ResponseEntity<SuccessResponse<String>> deleteUserDeprecated(@ApiIgnore @UserId Long userId,
		@Valid @RequestBody DeleteUserRequestDto request) {
		userService.deleteUserDeprecated(request, userId);
		return SuccessResponse.OK;
	}

	@ApiOperation(
		value = "[인증] 마이 페이지(설정) / 탈퇴 피드백 페이지 - 호미나라 피드백 보내기 혹은 탈퇴 피드백을 보냅니다.",
		notes = "내용이 존재할 경우에만 호출해주세요.\n"
			+ "피드백은 슬랙 알림으로 전달합니다.\n"
			+ "호미나라 피드백 보내기의 경우 isDeleting = false,\n"
			+ "탈퇴 피드백의 경우 isDeleting = true 를 보내주세요."
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "성공입니다."),
		@ApiResponse(code = 400,
			message = "1. 의견을 입력해주세요. (comment)\n"
				+ "2. 의견은 200 글자 이내로 입력해주세요. (comment)",
			response = ErrorResponse.class),
		@ApiResponse(code = 401, message = "토큰이 만료되었습니다. 다시 로그인 해주세요.", response = ErrorResponse.class),
		@ApiResponse(code = 404,
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
	@PostMapping("/v1/user/feedback")
	public ResponseEntity<SuccessResponse<String>> sendUserFeedback(@ApiIgnore @UserId Long userId,
		@Valid @RequestBody UserFeedbackRequestDto request) {
		userService.sendUserFeedback(userId, request);
		return SuccessResponse.OK;
	}

	@ApiOperation(
		value = "[인증] 마이 페이지(설정) - 회원 정보를 삭제합니다.",
		notes = "회원 정보 탈퇴 요청 시 해당 유저의 모든 정보를 삭제합니다.\n"
			+ "feedbackType을 NO를 보낸 경우, 사유가 없는 것으로 판단합니다. comment가 없는 경우 빈스트링(\"\")으로 보내주세요."
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "성공입니다."),
		@ApiResponse(code = 401, message = "토큰이 만료되었습니다. 다시 로그인 해주세요.", response = ErrorResponse.class),
		@ApiResponse(code = 404, message = "탈퇴했거나 존재하지 않는 유저입니다.", response = ErrorResponse.class),
		@ApiResponse(code = 409, message = "처리중인 요청입니다.", response = ErrorResponse.class),
		@ApiResponse(code = 426, message = "최신 버전으로 업그레이드가 필요합니다.", response = ErrorResponse.class),
		@ApiResponse(code = 500, message = "예상치 못한 서버 에러가 발생하였습니다.", response = ErrorResponse.class)
	})
	@PreventDuplicateRequest
	@Version
	@Auth
	@DeleteMapping("/v2/user")
	public ResponseEntity<SuccessResponse<String>> deleteUser(@ApiIgnore @UserId Long userId) {
		userService.deleteUser(userId);
		return SuccessResponse.OK;
	}
}
