package hous.server.controller.user;

import hous.server.common.dto.ErrorResponse;
import hous.server.common.dto.SuccessResponse;
import hous.server.common.success.SuccessCode;
import hous.server.config.interceptor.Auth;
import hous.server.config.resolver.UserId;
import hous.server.domain.personality.PersonalityColor;
import hous.server.service.notification.NotificationRetrieveService;
import hous.server.service.notification.dto.response.NotificationsInfoResponse;
import hous.server.service.user.UserRetrieveService;
import hous.server.service.user.dto.response.*;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@Api(tags = "User")
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1")
public class UserRetrieveController {

    private final UserRetrieveService userRetrieveService;
    private final NotificationRetrieveService notificationRetrieveService;

    @ApiOperation(
            value = "[인증] 마이 페이지(프로필 뷰) - 나의 프로필 정보를 확인합니다.",
            notes = "사용자가 아직 입력하지 않은 데이터의 경우 null 이 전달됩니다."
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "나의 프로필 정보 조회 성공입니다."),
            @ApiResponse(code = 401, message = "토큰이 만료되었습니다. 다시 로그인 해주세요.", response = ErrorResponse.class),
            @ApiResponse(code = 404,
                    message = "1. 탈퇴했거나 존재하지 않는 유저입니다.\n"
                            + "2. 참가중인 방이 존재하지 않습니다.",
                    response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "예상치 못한 서버 에러가 발생하였습니다.", response = ErrorResponse.class)
    })
    @Auth
    @GetMapping("/user")
    public ResponseEntity<SuccessResponse<UserInfoResponse>> getUserInfo(@ApiIgnore @UserId Long userId) {
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
    @GetMapping("/user/{onboardingId}")
    public ResponseEntity<SuccessResponse<UserInfoResponse>> getHomieInfo(@ApiParam(name = "onboardingId", value = "조회할 호미의 id", required = true, example = "1")
                                                                          @PathVariable Long onboardingId, @ApiIgnore @UserId Long userId) {
        return SuccessResponse.success(SuccessCode.GET_HOMIE_PROFILE_INFO_SUCCESS, userRetrieveService.getHomieInfo(onboardingId, userId));
    }

    @ApiOperation(
            value = "[인증] 마이 페이지(설정) - 나의 푸시 알림 설정 정보를 조회합니다.",
            notes = "푸시 알림 설정 정보를 조회합니다.\n" +
                    "Rules, Badge 설정으로는 ON, OFF 상태가 있습니다.\n" +
                    "Todo 관련 설정으로는 ON_ALL, ON_MY, OFF 상태가 있습니다."
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "푸시 알림 설정 정보 조회 성공입니다."),
            @ApiResponse(code = 401, message = "토큰이 만료되었습니다. 다시 로그인 해주세요.", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "탈퇴했거나 존재하지 않는 유저입니다.", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "예상치 못한 서버 에러가 발생하였습니다.", response = ErrorResponse.class)
    })
    @Auth
    @GetMapping("/user/push")
    public ResponseEntity<SuccessResponse<PushSettingResponse>> getUserPushSetting(@ApiIgnore @UserId Long userId) {
        return SuccessResponse.success(SuccessCode.GET_USER_PUSH_SETTING_SUCCESS, userRetrieveService.getUserPushSetting(userId));
    }

    @ApiOperation(
            value = "[인증] 룸메이트 정보 페이지(Hous 뷰에서 호미 카드 클릭) - 성향 정보를 확인합니다.",
            notes = "color 쿼리에 조회할 성향 색깔을 담아서 요청을 보냅니다.\n" +
                    "GRAY 에 대한 정보는 존재하지 않기 때문에 404 에러를 전달합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성향 정보 조회 성공입니다."),
            @ApiResponse(code = 401, message = "토큰이 만료되었습니다. 다시 로그인 해주세요.", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "GRAY 에 대한 성향 정보는 존재하지 않습니다.", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "예상치 못한 서버 에러가 발생하였습니다.", response = ErrorResponse.class)
    })
    @Auth
    @GetMapping("/user/personality")
    public ResponseEntity<SuccessResponse<PersonalityInfoResponse>> getPersonalityInfo(@ApiParam(name = "color", value = "조회할 성향 색깔", required = true, example = "RED")
                                                                                       @RequestParam PersonalityColor color) {
        return SuccessResponse.success(SuccessCode.GET_PERSONALITY_INFO_SUCCESS, userRetrieveService.getPersonalityInfo(color));
    }

    @ApiOperation(
            value = "[인증] 마이 페이지(Profile 뷰) - 성향테스트 정보를 조회합니다.",
            notes = "15 개의 질문을 리스트에 순서대로 담아서 테스트 내용을 전달합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성향테스트 정보 조회 성공입니다."),
            @ApiResponse(code = 401, message = "토큰이 만료되었습니다. 다시 로그인 해주세요.", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "예상치 못한 서버 에러가 발생하였습니다.", response = ErrorResponse.class)
    })
    @Auth
    @GetMapping("/user/personality/test")
    public ResponseEntity<SuccessResponse<List<PersonalityTestInfoResponse>>> getPersonalityTestInfo() {
        return SuccessResponse.success(SuccessCode.GET_PERSONALITY_TEST_INFO_SUCCESS, userRetrieveService.getPersonalityTestInfo());
    }

    @ApiOperation(
            value = "[인증] 마이 페이지(배지 목록 뷰) - 나의 배지 정보를 조회합니다.",
            notes = "대표 배지가 없는 경우, null을 보냅니다.\n" +
                    "배지 목록에서는 모든 배지 리스트를 전달합니다.\n" +
                    "내가 획득한 배지의 경우 isAcquired = true 입니다.\n" +
                    "신규 배지의 경우 isRead = false 입니다."
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "나의 배지 목록 조회 성공입니다."),
            @ApiResponse(code = 401, message = "토큰이 만료되었습니다. 다시 로그인 해주세요.", response = ErrorResponse.class),
            @ApiResponse(code = 404,
                    message = "1. 탈퇴했거나 존재하지 않는 유저입니다.\n"
                            + "2. 참가중인 방이 존재하지 않습니다.",
                    response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "예상치 못한 서버 에러가 발생하였습니다.", response = ErrorResponse.class)
    })
    @Auth
    @GetMapping("/user/badges")
    public ResponseEntity<SuccessResponse<MyBadgeInfoResponse>> getMyBadgeList(@ApiIgnore @UserId Long userId) {
        return SuccessResponse.success(SuccessCode.GET_BADGE_INFO_SUCCESS, userRetrieveService.getMyBadgeList(userId));
    }

    @ApiOperation(
            value = "[인증] 알림 페이지 - 알림 목록을 조회합니다.",
            notes = "알림 목록을 스크롤 페이지네이션으로 조회합니다.\n" +
                    "size 에는 스크롤 1회당 조회할 개수를 담습니다.\n" +
                    "최초 요청으로 lastNotificationId 에 Long 최대값을 담습니다.\n" +
                    "다음 스크롤의 lastNotificationId 에는 nextCursor 값을 담습니다.\n" +
                    "nextCursor = -1 일 경우 더이상 조회할 데이터가 없음을 의미합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "알림 목록 조회 성공입니다."),
            @ApiResponse(code = 401, message = "토큰이 만료되었습니다. 다시 로그인 해주세요.", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "탈퇴했거나 존재하지 않는 유저입니다.", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "예상치 못한 서버 에러가 발생하였습니다.", response = ErrorResponse.class)
    })
    @Auth
    @GetMapping("/user/notifications")
    public ResponseEntity<SuccessResponse<NotificationsInfoResponse>> getNotificationsInfo(@ApiParam(name = "size", value = "스크롤 1회당 조회할 개수", required = true, example = "10")
                                                                                           @RequestParam int size,
                                                                                           @ApiParam(name = "lastNotificationId", value = "마지막으로 조회된 notificationId", required = true, example = "100")
                                                                                           @RequestParam Long lastNotificationId,
                                                                                           @ApiIgnore @UserId Long userId) {
        return SuccessResponse.success(SuccessCode.GET_NOTIFICATIONS_INFO_SUCCESS, notificationRetrieveService.getNotificationsInfo(size, lastNotificationId, userId));
    }
}
