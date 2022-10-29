package hous.server.controller.rule;

import hous.server.common.dto.ErrorResponse;
import hous.server.common.dto.SuccessResponse;
import hous.server.config.interceptor.Auth;
import hous.server.config.resolver.UserId;
import hous.server.service.rule.RuleService;
import hous.server.service.rule.dto.request.CreateRuleRequestDto;
import hous.server.service.rule.dto.request.DeleteRuleReqeustDto;
import hous.server.service.rule.dto.request.UpdateRuleRequestDto;
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
            notes = "생성할 규칙을 resquest dto에 리스트 형태로 담아주세요."
    )
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "생성 성공입니다."),
            @ApiResponse(
                    code = 400,
                    message = "1. 규칙 내용을 입력해주세요.\n"
                            + "2. 규칙은 20 글자 이내로 입력해주세요.\n"
                            + "3. 규칙 리스트는 빈 배열을 보낼 수 없습니다. (ruleNames)",
                    response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "토큰이 만료되었습니다. 다시 로그인 해주세요.", response = ErrorResponse.class),
            @ApiResponse(code = 403, message = "rule 은 30개를 초과할 수 없습니다.", response = ErrorResponse.class),
            @ApiResponse(
                    code = 404,
                    message = "1. 탈퇴했거나 존재하지 않는 유저입니다.\n"
                            + "2. 참가중인 방이 존재하지 않습니다.",
                    response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "예상치 못한 서버 에러가 발생하였습니다.", response = ErrorResponse.class)
    })
    @Auth
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/rules")
    public ResponseEntity<SuccessResponse<String>> createRule(@Valid @RequestBody CreateRuleRequestDto request,
                                                              @ApiIgnore @UserId Long userId) {
        ruleService.createRule(request, userId);
        return SuccessResponse.CREATED;
    }

    @ApiOperation(
            value = "[인증] 규칙 페이지 - 규칙 여러 개의 정렬 및 내용을 수정합니다.",
            notes = "전체 규칙 id와 내용이 담긴 리스트를 정렬 순서에 따라 resquest dto에 리스트 형태로 담아주세요."
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공입니다."),
            @ApiResponse(code = 400, message = "규칙 리스트는 빈 배열을 보낼 수 없습니다. (rulesIdList)", response = ErrorResponse.class),
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
    @PutMapping("/rules")
    public ResponseEntity<SuccessResponse<String>> updateSortByRules(@Valid @RequestBody UpdateRuleRequestDto request,
                                                                     @ApiIgnore @UserId Long userId) {
        ruleService.updateRules(request, userId);
        return SuccessResponse.OK;
    }

    @ApiOperation(
            value = "[인증] 규칙 페이지 - 규칙 여러 개를 삭제합니다.",
            notes = "삭제할 규칙의 id만 resquest dto에 리스트 형태로 담아주세요."
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공입니다."),
            @ApiResponse(code = 400, message = "규칙 리스트는 빈 배열을 보낼 수 없습니다. (rulesIdList)", response = ErrorResponse.class),
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
    @DeleteMapping("/rules")
    public ResponseEntity<SuccessResponse<String>> deleteRules(@Valid @RequestBody DeleteRuleReqeustDto request,
                                                               @ApiIgnore @UserId Long userId) {
        ruleService.deleteRules(request, userId);
        return SuccessResponse.OK;
    }
}
