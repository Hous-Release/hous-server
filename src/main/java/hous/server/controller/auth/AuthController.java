package hous.server.controller.auth;

import hous.server.common.dto.ErrorResponse;
import hous.server.common.dto.SuccessResponse;
import hous.server.common.success.SuccessCode;
import hous.server.config.interceptor.Auth;
import hous.server.config.resolver.UserId;
import hous.server.controller.auth.dto.request.LoginRequestDto;
import hous.server.controller.auth.dto.request.SignUpRequestDto;
import hous.server.controller.auth.dto.response.LoginResponse;
import hous.server.service.auth.AuthService;
import hous.server.service.auth.AuthServiceProvider;
import hous.server.service.auth.CommonAuthService;
import hous.server.service.auth.CreateTokenService;
import hous.server.service.auth.dto.request.TokenRequestDto;
import hous.server.service.auth.dto.response.TokenResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

@Api(tags = "Auth")
@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class AuthController {

    private final AuthServiceProvider authServiceProvider;
    private final CreateTokenService createTokenService;
    private final CommonAuthService commonAuthService;

    @ApiOperation(
            value = "온보딩 페이지 - 회원가입을 요청합니다.",
            notes = "카카오 회원가입, 애플 회원가입을 요청합니다.\n" +
                    "socialType - KAKAO (카카오), APPLE (애플)"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "회원가입 성공입니다."),
            @ApiResponse(
                    code = 400,
                    message = "1. 유저의 socialType 를 입력해주세요. (socialType)\n"
                            + "2. access token 을 입력해주세요. (token)\n"
                            + "3. fcm token 을 입력해주세요. (fcmToken)\n"
                            + "4. 닉네임을 입력해주세요. (nickname)\n"
                            + "5. 닉네임은 최대 3글자까지 가능합니다. (nickname)\n"
                            + "6. 생년월일을 입력해주세요. (birthday)\n"
                            + "7. 생년월일을 공개 여부를 체크해주세요. (isPublic)",
                    response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "유효하지 않은 토큰입니다.", response = ErrorResponse.class),
            @ApiResponse(
                    code = 409,
                    message = "1. 이미 해당 계정으로 회원가입하셨습니다.\n   로그인 해주세요.\n"
                            + "2. fcm token 중복입니다.",
                    response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "예상치 못한 서버 에러가 발생하였습니다.", response = ErrorResponse.class)
    })
    @PostMapping("/auth/signup")
    public ResponseEntity<SuccessResponse<LoginResponse>> signUp(@Valid @RequestBody SignUpRequestDto request) {
        AuthService authService = authServiceProvider.getAuthService(request.getSocialType());
        Long userId = authService.signUp(request.toServiceDto());

        TokenResponse tokenInfo = createTokenService.createTokenInfo(userId);
        return SuccessResponse.success(SuccessCode.SIGNUP_SUCCESS, LoginResponse.of(userId, tokenInfo));
    }

    @ApiOperation(
            value = "로그인 페이지 - 로그인을 요청합니다.",
            notes = "카카오 로그인, 애플 로그인을 요청합니다.\n" +
                    "socialType - KAKAO (카카오), APPLE (애플)\n" +
                    "회원가입이 완료되지 않은 사용자일 경우 404 에러를 전달합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "로그인 성공입니다."),
            @ApiResponse(
                    code = 400,
                    message = "1. 유저의 socialType 를 입력해주세요. (socialType)\n"
                            + "2. access token 을 입력해주세요. (token)\n"
                            + "3. fcm token 을 입력해주세요. (fcmToken)",
                    response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "유효하지 않은 토큰입니다.", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "탈퇴했거나 존재하지 않는 유저입니다.", response = ErrorResponse.class),
            @ApiResponse(code = 409, message = "fcm token 중복입니다.", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "예상치 못한 서버 에러가 발생하였습니다.", response = ErrorResponse.class)
    })
    @PostMapping("/auth/login")
    public ResponseEntity<SuccessResponse<LoginResponse>> login(@Valid @RequestBody LoginRequestDto request) {
        AuthService authService = authServiceProvider.getAuthService(request.getSocialType());
        Long userId = authService.login(request.toServiceDto());

        TokenResponse tokenInfo = createTokenService.createTokenInfo(userId);
        return SuccessResponse.success(SuccessCode.LOGIN_SUCCESS, LoginResponse.of(userId, tokenInfo));
    }

    @ApiOperation(
            value = "JWT 인증 - Access Token 을 갱신합니다.",
            notes = "만료된 Access Token 을 Refresh Token 으로 갱신합니다.\n" +
                    "Refresh Token 이 유효하지 않거나 만료된 경우 갱신에 실패합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "토큰 갱신 성공입니다."),
            @ApiResponse(
                    code = 400,
                    message = "1. access token 을 입력해주세요. (accessToken)\n"
                            + "2. refresh token 을 입력해주세요. (refreshToken)",
                    response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "토큰이 만료되었습니다. 다시 로그인 해주세요.", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "탈퇴했거나 존재하지 않는 유저입니다.", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "예상치 못한 서버 에러가 발생하였습니다.", response = ErrorResponse.class)
    })
    @PostMapping("/auth/refresh")
    public ResponseEntity<SuccessResponse<TokenResponse>> reissue(@Valid @RequestBody TokenRequestDto request) {
        return SuccessResponse.success(SuccessCode.REISSUE_TOKEN_SUCCESS, createTokenService.reissueToken(request));
    }

    @ApiOperation(
            value = "[인증] 마이 페이지(설정) - 로그아웃을 요청합니다.",
            notes = "로그아웃 성공시 로그인 페이지로 이동합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공입니다."),
            @ApiResponse(code = 401, message = "토큰이 만료되었습니다. 다시 로그인 해주세요.", response = ErrorResponse.class),
            @ApiResponse(
                    code = 404,
                    message = "1. 탈퇴했거나 존재하지 않는 유저입니다.\n"
                            + "2. 참가중인 방이 존재하지 않습니다.",
                    response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "예상치 못한 서버 에러가 발생하였습니다.", response = ErrorResponse.class)
    })
    @Auth
    @PostMapping("/auth/logout")
    public ResponseEntity<SuccessResponse<String>> logout(@ApiIgnore @UserId Long userId) {
        commonAuthService.logout(userId);
        return SuccessResponse.OK;
    }
}
