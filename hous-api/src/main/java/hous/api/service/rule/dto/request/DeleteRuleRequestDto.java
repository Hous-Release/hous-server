package hous.api.service.rule.dto.request;

import java.util.List;

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
@Builder(access = AccessLevel.PRIVATE)
public class DeleteRuleRequestDto {

	@ApiModelProperty(value = "규칙 id 배열", example = "[12, 13, 14, ...]")
	@NotNull(message = "{rule.list.notNull}")
	@Size(min = Constraint.RULE_LIST_MIN, message = "{rule.list.min}")
	private List<Long> rulesIdList;

	public static DeleteRuleRequestDto of(List<Long> rulesIdList) {
		return DeleteRuleRequestDto.builder()
			.rulesIdList(rulesIdList)
			.build();
	}
}
