package hous.server.controller.user;

import hous.server.common.dto.ErrorResponse;
import hous.server.common.dto.SuccessResponse;
import hous.server.common.success.SuccessCode;
import hous.server.config.interceptor.Auth;
import hous.server.config.resolver.UserId;
import hous.server.domain.personality.PersonalityColor;
import hous.server.service.user.UserRetrieveService;
import hous.server.service.user.dto.response.CheckOnboardingInfoResponse;
import hous.server.service.user.dto.response.PersonalityInfoResponse;
import hous.server.service.user.dto.response.UserInfoResponse;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@Api(tags = "User")
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1")
public class UserRetrieveController {

    private final UserRetrieveService userRetrieveService;

    @ApiOperation(
            value = "[인증] 온보딩 페이지 - 나의 온보딩 정보 등록여부를 확인합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "온보딩 등록여부 조회 성공입니다."),
            @ApiResponse(code = 401, message = "토큰이 만료되었습니다. 다시 로그인 해주세요.", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "탈퇴했거나 존재하지 않는 유저입니다.", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "예상치 못한 서버 에러가 발생하였습니다.", response = ErrorResponse.class)
    })
    @Auth
    @GetMapping("/user/onboarding/check")
    public ResponseEntity<CheckOnboardingInfoResponse> checkMyOnboardingInfo(@ApiIgnore @UserId Long userId) {
        return SuccessResponse.success(SuccessCode.CHECK_ONBOARDING_SUCCESS, userRetrieveService.checkMyOnboardingInfo(userId));
    }

    @ApiOperation(
            value = "[인증] 마이 페이지(프로필 뷰) - 나의 프로필 정보를 확인합니다.",
            notes = "성공 시, 생년월일 공개 여부(birthdayPublic) false 일 경우, 생년월일(birthday)은 null 입니다.\n" +
                    "사용자가 아직 입력하지 않은 데이터의 경우 null 이 전달됩니다."
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "나의 프로필 정보 조회 성공입니다."),
            @ApiResponse(code = 401, message = "토큰이 만료되었습니다. 다시 로그인 해주세요.", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "탈퇴했거나 존재하지 않는 유저입니다.", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "예상치 못한 서버 에러가 발생하였습니다.", response = ErrorResponse.class)
    })
    @Auth
    @GetMapping("/user")
    public ResponseEntity<UserInfoResponse> getUserInfo(@ApiIgnore @UserId Long userId) {
        return SuccessResponse.success(SuccessCode.GET_MY_PROFILE_INFO_SUCCESS, userRetrieveService.getUserInfo(userId));
    }

    @ApiOperation(
            value = "[인증] 룸메이트 정보 페이지(Hous 뷰에서 호미 카드 클릭) - 룸메이트 프로필 정보를 확인합니다.",
            notes = "성공 시, 생년월일 공개 여부(birthdayPublic) false 일 경우, 생년월일(birthday)은 null 입니다.\n" +
                    "사용자가 아직 입력하지 않은 데이터의 경우 null 이 전달됩니다."
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "룸메이트 프로필 정보 조회 성공입니다."),
            @ApiResponse(code = 401, message = "토큰이 만료되었습니다. 다시 로그인 해주세요.", response = ErrorResponse.class),
            @ApiResponse(code = 403, message = "같은 방에 참가하고 있지 않습니다.", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "탈퇴했거나 존재하지 않는 유저입니다.", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "예상치 못한 서버 에러가 발생하였습니다.", response = ErrorResponse.class)
    })
    @Auth
    @GetMapping("/user/{homieId}")
    public ResponseEntity<UserInfoResponse> getHomieInfo(@ApiParam(name = "homieId", value = "조회할 호미의 id", required = true, example = "1")
                                                         @PathVariable Long homieId, @ApiIgnore @UserId Long userId) {
        return SuccessResponse.success(SuccessCode.GET_HOMIE_PROFILE_INFO_SUCCESS, userRetrieveService.getHomieInfo(homieId, userId));
    }

    @ApiOperation(
            value = "[인증] 룸메이트 정보 페이지(Hous 뷰에서 호미 카드 클릭) - 룸메이트 성향 정보를 확인합니다.",
            notes = "color 쿼리에 조회할 성향 색깔을 담아서 요청을 보냅니다.\n" +
                    "GRAY 에 대한 정보는 존재하지 않기 때문에 404 에러를 전달합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "룸메이트 성향 정보 조회 성공입니다."),
            @ApiResponse(code = 401, message = "토큰이 만료되었습니다. 다시 로그인 해주세요.", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "GRAY 에 대한 성향 정보는 존재하지 않습니다.", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "예상치 못한 서버 에러가 발생하였습니다.", response = ErrorResponse.class)
    })
    @Auth
    @GetMapping("/user/personality")
    public ResponseEntity<PersonalityInfoResponse> getHomiePersonalityInfo(@ApiParam(name = "color", value = "조회할 성향 색깔", required = true, example = "RED")
                                                                           @RequestParam PersonalityColor color) {
        return SuccessResponse.success(SuccessCode.GET_HOMIE_PERSONALITY_INFO_SUCCESS, userRetrieveService.getHomiePersonalityInfo(color));
    }
}
