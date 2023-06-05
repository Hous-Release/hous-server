package hous.api.controller.rule;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hous.api.config.interceptor.auth.Auth;
import hous.api.config.interceptor.version.Version;
import hous.api.config.resolver.UserId;
import hous.api.service.rule.RuleRetrieveService;
import hous.api.service.rule.dto.response.RuleAddableResponse;
import hous.api.service.rule.dto.response.RuleInfoResponse;
import hous.api.service.rule.dto.response.RulesResponse;
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

@Api(tags = "Rule")
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1")
public class RuleRetrieveController {

	private final RuleRetrieveService ruleRetrieveService;

	@ApiOperation(
		value = "[인증] 규칙 메인 페이지 - 방의 규칙을 조회합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "규칙 메인 페이지 조회 성공입니다."),
		@ApiResponse(code = 401, message = "토큰이 만료되었습니다. 다시 로그인 해주세요.", response = ErrorResponse.class),
		@ApiResponse(code = 404, message = "탈퇴했거나 존재하지 않는 유저입니다.", response = ErrorResponse.class),
		@ApiResponse(code = 426, message = "최신 버전으로 업그레이드가 필요합니다.", response = ErrorResponse.class),
		@ApiResponse(code = 500, message = "예상치 못한 서버 에러가 발생하였습니다.", response = ErrorResponse.class)
	})
	@Version
	@Auth
	@GetMapping("/rules")
	public ResponseEntity<SuccessResponse<RulesResponse>> getRulesInfo(@ApiIgnore @UserId Long userId) {
		return SuccessResponse.success(SuccessCode.GET_RULES_INFO_SUCCESS, ruleRetrieveService.getRulesInfo(userId));
	}

	@ApiOperation(
		value = "[인증] 규칙 메인 페이지 - 규칙 추가 가능 여부를 조회합니다.",
		notes = "규칙 개수가 30개 미만인 경우 true, 30개 이상인 경우 false 를 전달합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "규칙 추가 가능 여부 조회 성공입니다."),
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
	@GetMapping("/rule/addable")
	public ResponseEntity<SuccessResponse<RuleAddableResponse>> getTodoAddable(@ApiIgnore @UserId Long userId) {
		return SuccessResponse.success(SuccessCode.GET_RULE_ADDABLE_SUCCESS,
			ruleRetrieveService.getRuleAddable(userId));
	}

	@ApiOperation(
		value = "[인증] 규칙 개별 페이지 - 규칙 세부 정보를 조회합니다.",
		notes = "이미지가 없는 경우, 빈 배열([])을 전달합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "규칙 조회 성공입니다."),
		@ApiResponse(code = 401, message = "토큰이 만료되었습니다. 다시 로그인 해주세요.", response = ErrorResponse.class),
		@ApiResponse(
			code = 404,
			message = "1. 탈퇴했거나 존재하지 않는 유저입니다.\n"
				+ "2. 참가중인 방이 존재하지 않습니다.\n"
				+ "3. 존재하지 않는 규칙입니다.",
			response = ErrorResponse.class),
		@ApiResponse(code = 426, message = "최신 버전으로 업그레이드가 필요합니다.", response = ErrorResponse.class),
		@ApiResponse(code = 500, message = "예상치 못한 서버 에러가 발생하였습니다.", response = ErrorResponse.class)
	})
	@Version
	@Auth
	@GetMapping("/rule/{ruleId}")
	public ResponseEntity<SuccessResponse<RuleInfoResponse>> getRuleInfo(
		@ApiParam(name = "ruleId", value = "조회할 규칙의 id", required = true, example = "1")
		@PathVariable Long ruleId,
		@ApiIgnore @UserId Long userId) {
		return SuccessResponse.success(SuccessCode.GET_RULE_INFO_SUCCESS,
			ruleRetrieveService.getRuleInfo(userId, ruleId));
	}
}
