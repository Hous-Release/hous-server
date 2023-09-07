package hous.api.service.rule.dto.request;

import java.util.List;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateRuleRepresentRequestDto {

	@ApiModelProperty(value = "설정한 대표 규칙 id 리스트", example = "[1, 2, 3]")
	@NotNull(message = "{rule.list.notNull}")
	private List<Long> rules;
}
