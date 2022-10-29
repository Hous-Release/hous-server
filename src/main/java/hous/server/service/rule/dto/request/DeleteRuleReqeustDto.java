package hous.server.service.rule.dto.request;

import hous.server.domain.common.Constraint;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Size;
import java.util.List;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DeleteRuleReqeustDto {

    @ApiModelProperty(value = "규칙 id 배열", example = "[12, 13, 14, ...]")
    @Size(min = Constraint.RULE_LIST_MIN, message = "{rule.list.min}")
    private List<Long> rulesIdList;
}
