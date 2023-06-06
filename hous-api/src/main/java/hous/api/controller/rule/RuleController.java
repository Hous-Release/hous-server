package hous.api.controller.rule;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import hous.api.config.aop.duplicate.PreventDuplicateRequest;
import hous.api.config.interceptor.auth.Auth;
import hous.api.config.interceptor.version.Version;
import hous.api.config.resolver.UserId;
import hous.api.service.rule.RuleService;
import hous.api.service.rule.dto.request.CreateRuleInfoRequestDto;
import hous.api.service.rule.dto.request.CreateRuleRequestDto;
import hous.api.service.rule.dto.request.DeleteRuleRequestDto;
import hous.api.service.rule.dto.request.UpdateRuleInfoRequestDto;
import hous.api.service.rule.dto.request.UpdateRuleRepresentRequestDto;
import hous.api.service.rule.dto.request.UpdateRuleRequestDto;
import hous.common.dto.ErrorResponse;
import hous.common.dto.SuccessResponse;
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
public class RuleController {

	private final RuleService ruleService;

	// TODO Deprecated
	@ApiOperation(
		value = "@@ Deprecated 될 API 입니다. @@ [인증] 규칙 페이지 - 방의 규칙을 생성합니다.",
		notes = "생성할 규칙을 request dto 에 리스트 형태로 담아주세요."
	)
	@ApiResponses(value = {
		@ApiResponse(code = 201, message = "생성 성공입니다."),
		@ApiResponse(
			code = 400,
			message = "1. 규칙 내용을 입력해주세요.\n"
				+ "2. 규칙은 20 글자 이내로 입력해주세요.\n"
				+ "3. 규칙 리스트를 입력해주세요. (ruleNames)\n"
				+ "4. 규칙 리스트는 빈 배열을 보낼 수 없습니다. (ruleNames)",
			response = ErrorResponse.class),
		@ApiResponse(code = 401, message = "토큰이 만료되었습니다. 다시 로그인 해주세요.", response = ErrorResponse.class),
		@ApiResponse(code = 403, message = "rule 은 30개를 초과할 수 없습니다.", response = ErrorResponse.class),
		@ApiResponse(
			code = 404,
			message = "1. 탈퇴했거나 존재하지 않는 유저입니다.\n"
				+ "2. 참가중인 방이 존재하지 않습니다.",
			response = ErrorResponse.class),
		@ApiResponse(code = 409, message = "처리중인 요청입니다.", response = ErrorResponse.class),
		@ApiResponse(code = 426, message = "최신 버전으로 업그레이드가 필요합니다.", response = ErrorResponse.class),
		@ApiResponse(code = 500, message = "예상치 못한 서버 에러가 발생하였습니다.", response = ErrorResponse.class)
	})
	@PreventDuplicateRequest
	@Version
	@Auth
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/v1/rules")
	public ResponseEntity<SuccessResponse<String>> createRuleDeprecated(@ApiIgnore @UserId Long userId,
		@Valid @RequestBody CreateRuleRequestDto request) {
		ruleService.createRuleDeprecated(request, userId);
		return SuccessResponse.CREATED;
	}

	@ApiOperation(
		value = "[인증] 규칙 페이지 - 방의 규칙을 생성합니다.",
		notes = "생성할 규칙 정보를 request dto 에 리스트 형태로 담아주세요."
	)
	@ApiResponses(value = {
		@ApiResponse(code = 201, message = "생성 성공입니다."),
		@ApiResponse(
			code = 400,
			message = "1. 규칙 제목을 입력해주세요.\n"
				+ "2. 규칙 제목은 20 글자 이내로 입력해주세요.\n"
				+ "3. 규칙 설명은 50 글자 이내로 입력해주세요.\n"
				+ "4. 허용되지 않은 파일 형식입니다.\n"
				+ "5. 이미지가 (720x720) 보다 큽니다.\n"
				+ "6. 규칙 이미지는 최대 5 개 입니다.",
			response = ErrorResponse.class),
		@ApiResponse(code = 401, message = "토큰이 만료되었습니다. 다시 로그인 해주세요.", response = ErrorResponse.class),
		@ApiResponse(
			code = 404,
			message = "1. 탈퇴했거나 존재하지 않는 유저입니다.\n"
				+ "2. 존재하지 않는 규칙입니다.\n"
				+ "3. 존재하지 않는 배지 입니다.\n"
				+ "4. 참가중인 방이 존재하지 않습니다.",
			response = ErrorResponse.class),
		@ApiResponse(
			code = 409,
			message = "1. 처리중인 요청입니다.\n"
				+ "2. 이미 존재하는 규칙입니다.",
			response = ErrorResponse.class),
		@ApiResponse(code = 426, message = "최신 버전으로 업그레이드가 필요합니다.", response = ErrorResponse.class),
		@ApiResponse(code = 500, message = "예상치 못한 서버 에러가 발생하였습니다.", response = ErrorResponse.class)
	})
	@PreventDuplicateRequest
	@Version
	@Auth
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/v2/rule")
	public ResponseEntity<SuccessResponse<String>> createRule(@ApiIgnore @UserId Long userId,
		@Valid @ModelAttribute CreateRuleInfoRequestDto request,
		@ApiParam(name = "images", value = "규칙 이미지 리스트")
		@RequestPart(required = false, name = "images") List<MultipartFile> images) {
		ruleService.createRule(request, userId, images);
		return SuccessResponse.CREATED;
	}

	// TODO Deprecated
	@ApiOperation(
		value = "@@ Deprecated 될 API 입니다. @@ [인증] 규칙 페이지 - 규칙 여러 개의 정렬 및 내용을 수정합니다.",
		notes = "전체 규칙 id와 내용이 담긴 리스트를 정렬 순서에 따라 request dto 에 리스트 형태로 담아주세요."
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "성공입니다."),
		@ApiResponse(
			code = 400,
			message = "1. 규칙 내용을 입력해주세요.\n"
				+ "2. 규칙은 20 글자 이내로 입력해주세요.\n"
				+ "3. 규칙 리스트를 입력해주세요. (rules)\n"
				+ "4. 규칙 리스트는 빈 배열을 보낼 수 없습니다. (rules)",
			response = ErrorResponse.class),
		@ApiResponse(code = 401, message = "토큰이 만료되었습니다. 다시 로그인 해주세요.", response = ErrorResponse.class),
		@ApiResponse(
			code = 404,
			message = "1. 탈퇴했거나 존재하지 않는 유저입니다.\n"
				+ "2. 존재하지 않는 방입니다.\n"
				+ "3. 존재하지 않는 규칙입니다.",
			response = ErrorResponse.class),
		@ApiResponse(code = 409, message = "처리중인 요청입니다.", response = ErrorResponse.class),
		@ApiResponse(code = 426, message = "최신 버전으로 업그레이드가 필요합니다.", response = ErrorResponse.class),
		@ApiResponse(code = 500, message = "예상치 못한 서버 에러가 발생하였습니다.", response = ErrorResponse.class)
	})
	@PreventDuplicateRequest
	@Version
	@Auth
	@PutMapping("/v1/rules")
	public ResponseEntity<SuccessResponse<String>> updateRulesDeprecated(@ApiIgnore @UserId Long userId,
		@Valid @RequestBody UpdateRuleRequestDto request) {
		ruleService.updateRulesDeprecated(request, userId);
		return SuccessResponse.OK;
	}

	@ApiOperation(
		value = "[인증] 규칙 개별 페이지 - 규칙의 내용 및 사진을 수정합니다.",
		notes = "수정 사항이 없는 필드 및 사진도 모두 포함하여 request dto 형식에 맞춰 보내주세요."
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "성공입니다."),
		@ApiResponse(
			code = 400,
			message = "1. 규칙 제목을 입력해주세요.\n"
				+ "2. 규칙 제목은 20 글자 이내로 입력해주세요.\n"
				+ "3. 규칙 설명은 50 글자 이내로 입력해주세요.\n"
				+ "4. 허용되지 않은 파일 형식입니다.\n"
				+ "5. 이미지가 (720x720) 보다 큽니다.\n"
				+ "6. 규칙 이미지는 최대 5 개 입니다.",
			response = ErrorResponse.class),
		@ApiResponse(code = 401, message = "토큰이 만료되었습니다. 다시 로그인 해주세요.", response = ErrorResponse.class),
		@ApiResponse(
			code = 404,
			message = "1. 탈퇴했거나 존재하지 않는 유저입니다.\n"
				+ "2. 존재하지 않는 규칙입니다.\n"
				+ "3. 참가중인 방이 존재하지 않습니다.",
			response = ErrorResponse.class),
		@ApiResponse(
			code = 409,
			message = "1. 처리중인 요청입니다.\n"
				+ "2. 이미 존재하는 규칙입니다.",
			response = ErrorResponse.class),
		@ApiResponse(code = 426, message = "최신 버전으로 업그레이드가 필요합니다.", response = ErrorResponse.class),
		@ApiResponse(code = 500, message = "예상치 못한 서버 에러가 발생하였습니다.", response = ErrorResponse.class)
	})
	@PreventDuplicateRequest
	@Version
	@Auth
	@PutMapping("/v2/rule/{ruleId}")
	public ResponseEntity<SuccessResponse<String>> updateRule(@ApiIgnore @UserId Long userId,
		@ApiParam(name = "ruleId", value = "규칙 id") @PathVariable Long ruleId,
		@Valid @ModelAttribute UpdateRuleInfoRequestDto request,
		@ApiParam(name = "images", value = "규칙 이미지 리스트")
		@RequestPart(required = false, name = "images") List<MultipartFile> images) {
		ruleService.updateRule(request, ruleId, userId, images);
		return SuccessResponse.OK;
	}

	// TODO Deprecated
	@ApiOperation(
		value = "@@ Deprecated 될 API 입니다. @@ [인증] 규칙 페이지 - 규칙 여러 개를 삭제합니다.",
		notes = "삭제할 규칙의 id만 request dto 에 리스트 형태로 담아주세요."
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "성공입니다."),
		@ApiResponse(
			code = 400,
			message = "1. 규칙 리스트를 입력해주세요. (rulesIdList)\n"
				+ "2. 규칙 리스트는 빈 배열을 보낼 수 없습니다. (rulesIdList)",
			response = ErrorResponse.class),
		@ApiResponse(code = 401, message = "토큰이 만료되었습니다. 다시 로그인 해주세요.", response = ErrorResponse.class),
		@ApiResponse(
			code = 404,
			message = "1. 탈퇴했거나 존재하지 않는 유저입니다.\n"
				+ "2. 존재하지 않는 방입니다.\n"
				+ "3. 존재하지 않는 규칙입니다.",
			response = ErrorResponse.class),
		@ApiResponse(code = 409, message = "처리중인 요청입니다.", response = ErrorResponse.class),
		@ApiResponse(code = 426, message = "최신 버전으로 업그레이드가 필요합니다.", response = ErrorResponse.class),
		@ApiResponse(code = 500, message = "예상치 못한 서버 에러가 발생하였습니다.", response = ErrorResponse.class)
	})
	@PreventDuplicateRequest
	@Version
	@Auth
	@DeleteMapping("/v1/rules")
	public ResponseEntity<SuccessResponse<String>> deleteRules(@ApiIgnore @UserId Long userId,
		@Valid @RequestBody DeleteRuleRequestDto request) {
		ruleService.deleteRules(request, userId);
		return SuccessResponse.OK;
	}

	@ApiOperation(value = "[인증] 규칙 개별 페이지 - 규칙을 삭제합니다.")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "성공입니다."),
		@ApiResponse(code = 401, message = "토큰이 만료되었습니다. 다시 로그인 해주세요.", response = ErrorResponse.class),
		@ApiResponse(
			code = 404,
			message = "1. 탈퇴했거나 존재하지 않는 유저입니다.\n"
				+ "2. 참가중인 방이 존재하지 않습니다.\n"
				+ "3. 존재하지 않는 규칙입니다.",
			response = ErrorResponse.class),
		@ApiResponse(code = 409, message = "처리중인 요청입니다.", response = ErrorResponse.class),
		@ApiResponse(code = 426, message = "최신 버전으로 업그레이드가 필요합니다.", response = ErrorResponse.class),
		@ApiResponse(code = 500, message = "예상치 못한 서버 에러가 발생하였습니다.", response = ErrorResponse.class)
	})
	@PreventDuplicateRequest
	@Version
	@Auth
	@DeleteMapping("/v2/rule/{ruleId}")
	public ResponseEntity<SuccessResponse<String>> deleteRule(@ApiIgnore @UserId Long userId,
		@ApiParam(name = "ruleId", value = "규칙 id") @PathVariable Long ruleId) {
		ruleService.deleteRule(ruleId, userId);
		return SuccessResponse.OK;
	}

	@ApiOperation(
		value = "[인증] 대표 규칙 편집 페이지 - 대표 규칙을 설정합니다.",
		notes = "대표 규칙을 선택할 규칙 id 리스트를 request dto 로 전달해주세요."
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "성공입니다."),
		@ApiResponse(
			code = 400,
			message = "1. 규칙 리스트를 입력해주세요. (rulesIdList)\n"
				+ "2. 규칙 리스트는 빈 배열을 보낼 수 없습니다. (rulesIdList)",
			response = ErrorResponse.class),
		@ApiResponse(code = 401, message = "토큰이 만료되었습니다. 다시 로그인 해주세요.", response = ErrorResponse.class),
		@ApiResponse(code = 403, message = "대표 rule 은 3개를 초과할 수 없습니다.", response = ErrorResponse.class),
		@ApiResponse(
			code = 404,
			message = "1. 탈퇴했거나 존재하지 않는 유저입니다.\n"
				+ "2. 존재하지 않는 규칙입니다.",
			response = ErrorResponse.class),
		@ApiResponse(code = 409, message = "처리중인 요청입니다.", response = ErrorResponse.class),
		@ApiResponse(code = 426, message = "최신 버전으로 업그레이드가 필요합니다.", response = ErrorResponse.class),
		@ApiResponse(code = 500, message = "예상치 못한 서버 에러가 발생하였습니다.", response = ErrorResponse.class)
	})
	@PreventDuplicateRequest
	@Version
	@Auth
	@PutMapping("/v2/rules/represent")
	public ResponseEntity<SuccessResponse<String>> updateRepresentRule(@ApiIgnore @UserId Long userId,
		@Valid @RequestBody UpdateRuleRepresentRequestDto request) {
		ruleService.updateRepresentRule(request, userId);
		return SuccessResponse.OK;
	}
}
