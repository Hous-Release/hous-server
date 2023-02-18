package hous.server.service.rule.dto.request;

import hous.server.domain.common.Constraint;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class DeleteRuleReqeustDto {

    @ApiModelProperty(value = "규칙 id 배열", example = "[12, 13, 14, ...]")
    @NotNull(message = "{rule.list.notNull}")
    @Size(min = Constraint.RULE_LIST_MIN, message = "{rule.list.min}")
    private List<Long> rulesIdList;

    public static DeleteRuleReqeustDto of(List<Long> rulesIdList) {
        return DeleteRuleReqeustDto.builder()
                .rulesIdList(rulesIdList)
                .build();
    }
}
