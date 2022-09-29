package hous.server.service.rule.dto.request;

import hous.server.domain.common.Constraint;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UpdateRuleRequestDto {

    @ApiModelProperty(value = "규칙 내용", example = "우리집 대장은 김또순")
    @NotBlank(message = "{rule.name.notBlank")
    @Size(max = Constraint.RULE_NAME_MAX, message = "{rule.name.max}")
    private String name;
}
