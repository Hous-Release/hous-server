package hous.server.service.rule.dto.request;

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
public class ModifyRuleReqeustDto {

    @ApiModelProperty(value = "규칙 id 배열", example = "[12, 13, 14, ...]")
    @Size(min = 1, message = "{rule.list.min}")
    private List<Long> rulesIdList;
}
