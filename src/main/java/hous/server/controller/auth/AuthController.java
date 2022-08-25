package hous.server.controller.auth;

import hous.server.common.dto.ErrorResponse;
import hous.server.common.dto.SuccessResponse;
import hous.server.common.success.SuccessCode;
import hous.server.controller.auth.dto.request.LoginRequestDto;
import hous.server.controller.auth.dto.response.LoginResponse;
import hous.server.service.auth.AuthService;
import hous.server.service.auth.AuthServiceProvider;
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

import javax.validation.Valid;

@Api(tags = "Auth")
@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthServiceProvider authServiceProvider;
    private final CreateTokenService createTokenService;

    @ApiOperation(
            value = "로그인 페이지 - 로그인을 요청합니다.",
            notes = "카카오 로그인, 애플 로그인을 요청합니다.\n" +
                    "최초 로그인의 경우 회원가입 처리 후 로그인됩니다.\n" +
                    "socialType - KAKAO (카카오), APPLE (애플)\n"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "로그인 성공입니다."),
            @ApiResponse(
                    code = 400,
                    message = "1. 유저의 socialType 를 입력해주세요.\n"
                            + "2. access token 을 입력해주세요.\n"
                            + "3. fcm token 을 입력해주세요.",
                    response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "유효하지 않은 토큰입니다.", response = ErrorResponse.class),
            @ApiResponse(code = 409, message = "이미 해당 계정으로 회원가입하셨습니다.\n로그인 해주세요.", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "예상치 못한 서버 에러가 발생하였습니다.", response = ErrorResponse.class)
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequestDto request) {
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
                    message = "1. access token 을 입력해주세요.\n"
                            + "2. refresh token 을 입력해주세요.",
                    response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "토큰이 만료되었습니다. 다시 로그인 해주세요.", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "예상치 못한 서버 에러가 발생하였습니다.", response = ErrorResponse.class)
    })
    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> reissue(@Valid @RequestBody TokenRequestDto request) {
        return SuccessResponse.success(SuccessCode.REISSUE_TOKEN_SUCCESS, createTokenService.reissueToken(request));
    }
}
