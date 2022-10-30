package hous.server.service.rule.dto.request;

import hous.server.domain.common.Constraint;
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
public class CreateRuleRequestDto {

    @ApiModelProperty(value = "규칙 내용 배열", example = "[\"우리집 대장은 김또순\", \"우리집 대장은 혜조니\", \"우리집 대장은 혁주니\", ...]")
    @NotNull(message = "{rule.list.notNull}")
    @Size(min = Constraint.RULE_LIST_MIN, message = "{rule.list.min}")
    private List<String> ruleNames;
}
