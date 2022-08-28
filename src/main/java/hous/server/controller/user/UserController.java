package hous.server.controller.user;

import hous.server.common.dto.ErrorResponse;
import hous.server.common.dto.SuccessResponse;
import hous.server.common.success.SuccessCode;
import hous.server.config.interceptor.Auth;
import hous.server.config.resolver.UserId;
import hous.server.service.user.UserService;
import hous.server.service.user.dto.request.SetOnboardingInfoRequestDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@Api(tags = "User")
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/user")
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
                + "3. 생년월일을 입력해주세요. (birthday)",
            response = ErrorResponse.class),
        @ApiResponse(code = 401, message = "토큰이 만료되었습니다. 다시 로그인 해주세요.", response = ErrorResponse.class),
        @ApiResponse(code = 404, message = "탈퇴했거나 존재하지 않는 유저입니다.", response = ErrorResponse.class),
        @ApiResponse(code = 500, message = "예상치 못한 서버 에러가 발생하였습니다.", response = ErrorResponse.class)
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Auth
    @PostMapping("/onboarding")
    public ResponseEntity<SuccessResponse> setOnboardingInfo(
        @Valid @RequestBody SetOnboardingInfoRequestDto request, @ApiIgnore @UserId Long userId) {
        userService.setOnboardingInfo(request, userId);
        return SuccessResponse.success(SuccessCode.NO_CONTENT_SUCCESS, null);
    }

}
