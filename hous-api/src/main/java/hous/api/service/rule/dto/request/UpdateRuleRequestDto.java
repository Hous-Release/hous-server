package hous.api.service.rule.dto.request;

import hous.api.service.rule.dto.response.RuleInfo;
import hous.common.constant.Constraint;
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
    private List<RuleInfo> rules;
}
