package hous.server.controller.rule;

import hous.server.common.dto.ErrorResponse;
import hous.server.common.dto.SuccessResponse;
import hous.server.common.success.SuccessCode;
import hous.server.config.interceptor.Auth;
import hous.server.config.resolver.UserId;
import hous.server.service.rule.RuleService;
import hous.server.service.rule.dto.request.CreateRuleRequestDto;
import hous.server.service.rule.dto.response.RuleInfoResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

@Api(tags = "Rule")
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1")
public class RuleController {

    private final RuleService ruleService;

    @ApiOperation(
            value = "[인증] 규칙 페이지 - 방의 규칙을 생성합니다.",
            notes = "성공시 status code = 201, response body로 해당 규칙의 idx와 규칙 내용을 보냅니다."
    )
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "규칙 생성을 성공입니다."),
            @ApiResponse(
                    code = 400,
                    message = "1. 규칙 내용을 입력해주세요. (name)\n"
                            + "2. 규칙은 20 글자 이내로 입력해주세요. (name)",
                    response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "토큰이 만료되었습니다. 다시 로그인 해주세요.", response = ErrorResponse.class),
            @ApiResponse(
                    code = 404,
                    message = "1. 탈퇴했거나 존재하지 않는 유저입니다.\n"
                            + "2. 존재하지 않는 방입니다.",
                    response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "예상치 못한 서버 에러가 발생하였습니다.", response = ErrorResponse.class)
    })
    @Auth
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("/rule")
    public ResponseEntity<RuleInfoResponse> createRule(@Valid @RequestBody CreateRuleRequestDto request,
                                                       @ApiIgnore @UserId Long userId) {
        return SuccessResponse.success(SuccessCode.CREATE_RULE_SUCCESS, ruleService.createRule(request, userId));
    }

}