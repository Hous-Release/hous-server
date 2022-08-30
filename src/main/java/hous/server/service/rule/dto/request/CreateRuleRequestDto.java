package hous.server.service.rule.dto.request;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateRuleRequestDto {

    @ApiModelProperty(value = "규칙 내용", example = "우리집 대장은 김또순")
    @NotBlank(message = "{rule.name.notBlank")
    @Size(max = 20, message = "{rule.name.max}")
    private String name;
}
