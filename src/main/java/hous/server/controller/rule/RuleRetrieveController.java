package hous.server.controller.rule;

import hous.server.common.dto.ErrorResponse;
import hous.server.common.dto.SuccessResponse;
import hous.server.common.success.SuccessCode;
import hous.server.config.interceptor.Auth;
import hous.server.config.resolver.UserId;
import hous.server.service.rule.RuleRetrieveService;
import hous.server.service.rule.dto.response.RuleInfoResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@Api(tags = "Rule")
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1")
public class RuleRetrieveController {

    private final RuleRetrieveService ruleRetrieveService;

    @ApiOperation(
            value = "[인증] 규칙 페이지 - 방의 규칙을 조회합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "규칙 조회 성공입니다."),
            @ApiResponse(code = 401, message = "토큰이 만료되었습니다. 다시 로그인 해주세요.", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "탈퇴했거나 존재하지 않는 유저입니다.", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "예상치 못한 서버 에러가 발생하였습니다.", response = ErrorResponse.class)
    })
    @Auth
    @GetMapping("/rules")
    public ResponseEntity<SuccessResponse<List<RuleInfoResponse>>> getRulesInfo(@ApiIgnore @UserId Long userId) {
        return SuccessResponse.success(SuccessCode.GET_RULE_INFO_SUCCESS, ruleRetrieveService.getRulesInfo(userId));
    }
}
