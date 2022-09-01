package hous.server.controller.rule;

import hous.server.common.dto.ErrorResponse;
import hous.server.common.dto.SuccessResponse;
import hous.server.config.interceptor.Auth;
import hous.server.config.resolver.UserId;
import hous.server.service.rule.RuleService;
import hous.server.service.rule.dto.request.CreateRuleRequestDto;
import hous.server.service.rule.dto.request.UpdateRuleRequestDto;
import io.swagger.annotations.*;
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
            notes = "성공시 status code = 204, 빈 response body로 보냅니다."
    )
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = ""),
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
    public ResponseEntity<String> createRule(@Valid @RequestBody CreateRuleRequestDto request,
                                             @ApiIgnore @UserId Long userId) {
        ruleService.createRule(request, userId);
        return SuccessResponse.NO_CONTENT;
    }

    @ApiOperation(
            value = "[인증] 규칙 페이지 - 규칙 1개를 수정합니다.",
            notes = "성공시 status code = 204, 빈 response body를 보냅니다."
    )
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "규칙 생성을 성공입니다."),
            @ApiResponse(
                    code = 400,
                    message = "1. 규칙 내용을 입력해주세요. (name)\n"
                            + "2. 규칙은 20 글자 이내로 입력해주세요. (name)",
                    response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "토큰이 만료되었습니다. 다시 로그인 해주세요.", response = ErrorResponse.class),
            @ApiResponse(
                    code = 404,
                    message = "1. 탈퇴했거나 존재하지 않는 유저입니다.\n"
                            + "2. 존재하지 않는 방입니다.\n"
                            + "3. 존재하지 않는 규칙입니다.",
                    response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "예상치 못한 서버 에러가 발생하였습니다.", response = ErrorResponse.class)
    })
    @Auth
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("/rule/{ruleId}")
    public ResponseEntity<String> UpdateRule(@ApiParam(name = "ruleId", value = "수정할 rule 의 id", required = true, example = "1")
                                             @PathVariable Long ruleId,
                                             @Valid @RequestBody UpdateRuleRequestDto request,
                                             @ApiIgnore @UserId Long userId) {
        ruleService.updateRule(request, ruleId, userId);
        return SuccessResponse.NO_CONTENT;
    }
}
