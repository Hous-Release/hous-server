package hous.server.controller.user;

import hous.server.common.dto.ErrorResponse;
import hous.server.common.dto.SuccessResponse;
import hous.server.common.success.SuccessCode;
import hous.server.config.interceptor.Auth;
import hous.server.config.resolver.UserId;
import hous.server.service.user.UserService;
import hous.server.service.user.dto.request.DeleteUserRequestDto;
import hous.server.service.user.dto.request.UpdatePushSettingRequestDto;
import hous.server.service.user.dto.request.UpdateTestScoreRequestDto;
import hous.server.service.user.dto.request.UpdateUserInfoRequestDto;
import hous.server.service.user.dto.response.UpdatePersonalityColorResponse;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

@Api(tags = "User")
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1")
public class UserController {

    private final UserService userService;

    @ApiOperation(
            value = "[인증] 마이 페이지(Profile 뷰) - 나의 프로필 정보를 수정합니다.",
            notes = "프로필 정보 수정을 요청합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공입니다."),
            @ApiResponse(
                    code = 400,
                    message = "1. 닉네임을 입력해주세요. (nickname)\n"
                            + "2. 닉네임은 최대 5글자까지 가능합니다. (nickname)\n"
                            + "3. 생년월일을 입력해주세요. (birthday)\n"
                            + "4. 생년월일을 공개 여부를 체크해주세요. (isPublic)\n"
                            + "5. mbti 는 4 글자 이내로 입력해주세요. (mbti)\n"
                            + "6. 직업은 3 글자 이내로 입력해주세요. (job)\n"
                            + "7. 자기소개는 40 글자 이내로 입력해주세요. (introduction)",
                    response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "토큰이 만료되었습니다. 다시 로그인 해주세요.", response = ErrorResponse.class),
            @ApiResponse(code = 404,
                    message = "1. 탈퇴했거나 존재하지 않는 유저입니다. \n"
                            + "2. 존재하지 않는 방입니다.",
                    response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "예상치 못한 서버 에러가 발생하였습니다.", response = ErrorResponse.class)
    })
    @Auth
    @PutMapping("/user")
    public ResponseEntity<SuccessResponse<String>> updateUserInfo(@Valid @RequestBody UpdateUserInfoRequestDto request, @ApiIgnore @UserId Long userId) {
        userService.updateUserInfo(request, userId);
        return SuccessResponse.OK;
    }

    @ApiOperation(
            value = "[인증] 마이 페이지(설정) - 나의 푸시 알림 설정 정보를 수정합니다.",
            notes = "푸시 알림 설정 여부를 설정합니다.\n" +
                    "6가지 상태값 중 수정할 상태값 1개만 담아서 요청합니다.\n" +
                    "Rules, Badge 설정으로는 ON, OFF 를 담습니다.\n" +
                    "Todo 관련 설정으로는 ON_ALL, ON_MY, OFF 를 담습니다."
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공입니다."),
            @ApiResponse(code = 400, message = "잘못된 상태로 요청했습니다.", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "토큰이 만료되었습니다. 다시 로그인 해주세요.", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "탈퇴했거나 존재하지 않는 유저입니다.", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "예상치 못한 서버 에러가 발생하였습니다.", response = ErrorResponse.class)
    })
    @Auth
    @PatchMapping("/user/push")
    public ResponseEntity<SuccessResponse<String>> updateUserPushSetting(@Valid @RequestBody UpdatePushSettingRequestDto request, @ApiIgnore @UserId Long userId) {
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
                    code = 400, message = "성향 테스트의 각 성향 점수는 최소 3점, 최대 9점입니다. (smell, light, noise, clean, introversion)", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "토큰이 만료되었습니다. 다시 로그인 해주세요.", response = ErrorResponse.class),
            @ApiResponse(code = 404,
                    message = "1. 탈퇴했거나 존재하지 않는 유저입니다. \n"
                            + "2. 같은 방에 참가하고 있지 않습니다.",
                    response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "예상치 못한 서버 에러가 발생하였습니다.", response = ErrorResponse.class)
    })
    @Auth
    @PutMapping("/user/personality")
    public ResponseEntity<SuccessResponse<UpdatePersonalityColorResponse>> updateUserTestScore(
            @Valid @RequestBody UpdateTestScoreRequestDto request, @ApiIgnore @UserId Long userId) {
        return SuccessResponse.success(SuccessCode.UPDATE_PERSONALITY_TEST_SUCCESS, userService.updateUserTestScore(request, userId));
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
            @ApiResponse(code = 500, message = "예상치 못한 서버 에러가 발생하였습니다.", response = ErrorResponse.class)
    })
    @Auth
    @PutMapping("/user/badge/{badgeId}/represent")
    public ResponseEntity<SuccessResponse<String>> updateRepresentBadge(@ApiParam(name = "badgeId", value = "대표 배지로 설정할 badge 의 id", required = true, example = "1")
                                                                        @PathVariable Long badgeId,
                                                                        @ApiIgnore @UserId Long userId) {
        userService.updateRepresentBadge(badgeId, userId);
        return SuccessResponse.OK;
    }

    @ApiOperation(
            value = "[인증] 마이 페이지(설정) - 회원 정보를 삭제합니다.",
            notes = "회원 정보 탈퇴 요청 시 해당 유저의 모든 정보를 삭제합니다.\n"
                    + "feedbackType을 NO를 보낸 경우, 사유가 없는 것으로 판단합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공입니다."),
            @ApiResponse(code = 400, message = "사유를 선택 안한 경우, NO를 보내주세요. (feedbackType)", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "토큰이 만료되었습니다. 다시 로그인 해주세요.", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "탈퇴했거나 존재하지 않는 유저입니다.", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "예상치 못한 서버 에러가 발생하였습니다.", response = ErrorResponse.class)
    })
    @Auth
    @DeleteMapping("/user")
    public ResponseEntity<SuccessResponse<String>> deleteUser(@RequestBody DeleteUserRequestDto request, @ApiIgnore @UserId Long userId) {
        userService.deleteUser(request, userId);
        return SuccessResponse.OK;
    }

    @ApiOperation(
            value = "[인증] 마이 페이지(설정) - 피드백 보내기 버튼 클릭 시 피드백 한걸음 배지를 전달 받습니다.",
            notes = "배지는 푸시알림으로 전달합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공입니다."),
            @ApiResponse(code = 401, message = "토큰이 만료되었습니다. 다시 로그인 해주세요.", response = ErrorResponse.class),
            @ApiResponse(code = 404,
                    message = "1. 탈퇴했거나 존재하지 않는 유저입니다.\n"
                            + "2. 참가중인 방이 존재하지 않습니다.",
                    response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "예상치 못한 서버 에러가 발생하였습니다.", response = ErrorResponse.class)
    })
    @Auth
    @PostMapping("/user/feedback")
    public ResponseEntity<SuccessResponse<String>> acquireFeedbackBadge(@ApiIgnore @UserId Long userId) {
        userService.acquireFeedbackBadge(userId);
        return SuccessResponse.OK;
    }
}
