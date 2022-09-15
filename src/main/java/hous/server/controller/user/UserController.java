package hous.server.controller.user;

import hous.server.common.dto.ErrorResponse;
import hous.server.common.dto.SuccessResponse;
import hous.server.config.interceptor.Auth;
import hous.server.config.resolver.UserId;
import hous.server.service.user.UserService;
import hous.server.service.user.dto.request.SetOnboardingInfoRequestDto;
import hous.server.service.user.dto.request.UpdateTestScoreRequestDto;
import hous.server.service.user.dto.request.UpdateUserInfoRequestDto;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
            value = "[인증] 온보딩 페이지 - 나의 온보딩 정보를 설정합니다.",
            notes = "닉네임, 생년월일을 설정합니다. 성공시 status code = 204, 빈 response body를 보냅니다."
    )
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = ""),
            @ApiResponse(
                    code = 400,
                    message = "1. 닉네임을 입력해주세요. (nickname)\n"
                            + "2. 닉네임은 최대 5글자까지 가능합니다. (nickname)\n"
                            + "3. 생년월일을 입력해주세요. (birthday)\n"
                            + "4. 생년월일을 공개 여부를 체크해주세요. (isPublic)",
                    response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "토큰이 만료되었습니다. 다시 로그인 해주세요.", response = ErrorResponse.class),
            @ApiResponse(code = 404,
                    message = "1. 탈퇴했거나 존재하지 않는 유저입니다. \n"
                            + "2. 같은 방에 참가하고 있지 않습니다.",
                    response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "예상치 못한 서버 에러가 발생하였습니다.", response = ErrorResponse.class)
    })
    @Auth
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("/user/onboarding")
    public ResponseEntity<String> setOnboardingInfo(
            @Valid @RequestBody SetOnboardingInfoRequestDto request, @ApiIgnore @UserId Long userId) {
        userService.setOnboardingInfo(request, userId);
        return SuccessResponse.NO_CONTENT;
    }

    @ApiOperation(
            value = "[인증] 마이 페이지(Profile 뷰) - 나의 프로필 정보를 수정합니다.",
            notes = "프로필 정보를 설정합니다. 성공시 status code = 204, 빈 response body를 보냅니다."
    )
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = ""),
            @ApiResponse(
                    code = 400,
                    message = "1. 닉네임을 입력해주세요. (nickname)\n"
                            + "2. 닉네임은 최대 5글자까지 가능합니다. (nickname)\n"
                            + "3. 생년월일을 입력해주세요. (birthday)\n"
                            + "4. 생년월일을 공개 여부를 체크해주세요. (isPublic)\n"
                            + "5. mbti 를 입력해주세요. (mbti)\n"
                            + "6. mbti 는 4 글자 이내로 입력해주세요. (mbti)\n"
                            + "7. 직업을 입력해주세요. (job)\n"
                            + "8. 직업은 3 글자 이내로 입력해주세요. (job)\n"
                            + "9. 자기소개를 입력해주세요.(introduction)\n"
                            + "10. 자기소개는 40 글자 이내로 입력해주세요. (introduction)",
                    response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "토큰이 만료되었습니다. 다시 로그인 해주세요.", response = ErrorResponse.class),
            @ApiResponse(code = 404,
                    message = "1. 탈퇴했거나 존재하지 않는 유저입니다. \n"
                            + "2. 존재하지 않는 방입니다.",
                    response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "예상치 못한 서버 에러가 발생하였습니다.", response = ErrorResponse.class)
    })
    @Auth
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/user")
    public ResponseEntity<String> updateUserInfo(
            @Valid @RequestBody UpdateUserInfoRequestDto request, @ApiIgnore @UserId Long userId) {
        userService.updateUserInfo(request, userId);
        return SuccessResponse.NO_CONTENT;
    }

    // TODO 푸쉬알림 설정뷰 확정나면 수정하기
    @ApiOperation(
            value = "[인증] 마이 페이지(Profile 뷰) - 나의 푸쉬 알림 설정 정보를 수정합니다.",
            notes = "푸쉬 알림 설정 여부를 설정합니다. 성공시 status code = 204, 빈 response body를 보냅니다."
    )
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = ""),
            @ApiResponse(
                    code = 400, message = "자기소개는 40 글자 이내로 입력해주세요. (introduction)", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "토큰이 만료되었습니다. 다시 로그인 해주세요.", response = ErrorResponse.class),
            @ApiResponse(code = 404,
                    message = "1. 탈퇴했거나 존재하지 않는 유저입니다. \n"
                            + "2. 존재하지 않는 방입니다.",
                    response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "예상치 못한 서버 에러가 발생하였습니다.", response = ErrorResponse.class)
    })
    @Auth
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/user/push")
    public ResponseEntity<String> updateUserInfo(@ApiParam(name = "state", value = "푸쉬 알림 여부", required = true, example = "true")
                                                 @RequestParam boolean state, @ApiIgnore @UserId Long userId) {
        userService.updateUserPushState(state, userId);
        return SuccessResponse.NO_CONTENT;
    }

    @ApiOperation(
            value = "[인증] 마이 페이지(Profile 뷰) - 성향테스트 결과 정보를 수정합니다.",
            notes = "성향테스트 결과를 수정합니다. 성공시 status code = 204, 빈 response body를 보냅니다."
    )
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = ""),
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
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/user/personality")
    public ResponseEntity<String> updateUserTestScore(
            @Valid @RequestBody UpdateTestScoreRequestDto request, @ApiIgnore @UserId Long userId) {
        userService.updateUserTestScore(request, userId);
        return SuccessResponse.NO_CONTENT;
    }

    @ApiOperation(
            value = "[인증] 마이 페이지(배지 목록 뷰) - 대표 배지를 설정합니다.",
            notes = "대표 배지를 설정합니다. 성공시 status code = 204, 빈 response body를 보냅니다."
    )
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = ""),
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
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/user/badge/{badgeId}/represent")
    public ResponseEntity<String> updateRepresentBadge(@ApiParam(name = "badgeId", value = "대표 뱃지로 설정할 badge 의 id", required = true, example = "1")
                                                       @PathVariable Long badgeId,
                                                       @ApiIgnore @UserId Long userId) {
        userService.updateRepresentBadge(badgeId, userId);
        return SuccessResponse.NO_CONTENT;
    }
}
