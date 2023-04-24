package hous.api.controller.version;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hous.api.service.version.VersionRetrieveService;
import hous.api.service.version.dto.response.VersionInfoResponse;
import hous.common.dto.ErrorResponse;
import hous.common.dto.SuccessResponse;
import hous.common.success.SuccessCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;

@Api(tags = "Version")
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1")
public class VersionRetrieveController {

	private final VersionRetrieveService versionRetrieveService;

	@ApiOperation(
		value = "버전 확인 - 강제 업데이트 필요 여부를 확인합니다.",
		notes = "iOS/AOS 별로 강제 업데이트 필요 여부, 마켓 링크를 전달합니다.\n"
			+ "헤더에 HousOsType, HousVersion 을 담아서 보내주세요."

	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "강제 업데이트 필요 여부 조회 성공입니다."),
		@ApiResponse(code = 400,
			message = "1. 잘못된 OS 타입 요청입니다.\n"
				+ "2. 잘못된 버전 형식입니다.",
			response = ErrorResponse.class),
		@ApiResponse(code = 404, message = "배포되지 않은 OS 입니다.", response = ErrorResponse.class),
		@ApiResponse(code = 500, message = "예상치 못한 서버 에러가 발생하였습니다.", response = ErrorResponse.class)
	})
	@GetMapping("/version")
	public ResponseEntity<SuccessResponse<VersionInfoResponse>> getVersionInfo(
		@RequestHeader("HousOsType") String requestOs,
		@RequestHeader("HousVersion") String requestVersion) {
		return SuccessResponse.success(SuccessCode.GET_VERSION_INFO_SUCCESS,
			versionRetrieveService.getVersionInfo(requestOs, requestVersion));
	}
}
