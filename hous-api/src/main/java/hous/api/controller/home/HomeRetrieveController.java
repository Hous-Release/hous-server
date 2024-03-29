package hous.api.controller.home;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hous.api.config.interceptor.auth.Auth;
import hous.api.config.interceptor.version.Version;
import hous.api.config.resolver.UserId;
import hous.api.service.home.HomeRetrieveService;
import hous.api.service.home.dto.response.HomeInfoResponse;
import hous.common.dto.ErrorResponse;
import hous.common.dto.SuccessResponse;
import hous.common.success.SuccessCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import springfox.documentation.annotations.ApiIgnore;

@Api(tags = "Home")
@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class HomeRetrieveController {

	private final HomeRetrieveService homeRetrieveService;

	@ApiOperation(
		value = "[인증] Hous- 페이지 - 홈 화면 정보를 조회합니다.",
		notes = "오늘 우리의 to-do 진행률, MY to-do, Our Rules, Homies 정보를 조회합니다.\n"
			+ "나의 성향 테스트 진행 여부 (isPersonalityTest) 에 대한 boolean 값이 추가되었습니다. \n"
			+ "성향 테스트를 한 경우 true, 아닌 경우 false 를 전달합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "홈 화면 정보 조회 성공입니다."),
		@ApiResponse(code = 401, message = "토큰이 만료되었습니다. 다시 로그인 해주세요.", response = ErrorResponse.class),
		@ApiResponse(
			code = 404,
			message = "1. 탈퇴했거나 존재하지 않는 유저입니다.\n"
				+ "2. 참가중인 방이 존재하지 않습니다.",
			response = ErrorResponse.class),
		@ApiResponse(code = 426, message = "최신 버전으로 업그레이드가 필요합니다.", response = ErrorResponse.class),
		@ApiResponse(code = 500, message = "예상치 못한 서버 에러가 발생하였습니다.", response = ErrorResponse.class)
	})
	@Version
	@Auth
	@GetMapping("/home")
	public ResponseEntity<SuccessResponse<HomeInfoResponse>> getHomeInfo(@ApiIgnore @UserId Long userId) {
		return SuccessResponse.success(SuccessCode.GET_HOME_INFO_SUCCESS, homeRetrieveService.getHomeInfo(userId));
	}
}
