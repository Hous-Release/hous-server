package hous.api.service.rule.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import hous.common.constant.Constraint;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PUBLIC)
public class CreateRuleInfoRequestDto {

	@ApiModelProperty(value = "규칙 내용", example = "우리집 대장은 김또순")
	@NotNull(message = "{rule.list.notNull}")
	@Size(min = Constraint.RULE_NAME_MIN, message = "{rule.name.min}")
	private String name;

	@Size(max = Constraint.RULE_DESCRIPTION_MAX, message = "{rule.description.max}")
	private String description;
}
