package hous.server.service.rule.dto.request;

import hous.server.domain.common.Constraint;
import hous.server.service.rule.dto.response.RuleInfoResponse;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UpdateRuleRequestDto {

    @ApiModelProperty(value = "수정된 규칙 리스트")
    @NotNull(message = "{rule.list.notNull}")
    @Size(min = Constraint.RULE_LIST_MIN, message = "{rule.list.min}")
    private List<RuleInfoResponse> rules;
}
