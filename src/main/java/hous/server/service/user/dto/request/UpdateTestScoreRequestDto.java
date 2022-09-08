package hous.server.service.user.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateTestScoreRequestDto {

    @ApiModelProperty(value = "빛", example = "15")
    @Min(value = 3, message = "{user.testScore.size}")
    @Max(value = 9, message = "{user.testScore.size}")
    private int light;

    @ApiModelProperty(value = "소음", example = "19")
    @Min(value = 3, message = "{user.testScore.size}")
    @Max(value = 9, message = "{user.testScore.size}")
    private int noise;

    @ApiModelProperty(value = "정리정돈", example = "3")
    @Min(value = 3, message = "{user.testScore.size}")
    @Max(value = 9, message = "{user.testScore.size}")
    private int clean;

    @ApiModelProperty(value = "냄새", example = "4")
    @Min(value = 3, message = "{user.testScore.size}")
    @Max(value = 9, message = "{user.testScore.size}")
    private int smell;

    @ApiModelProperty(value = "내향", example = "5")
    @Min(value = 3, message = "{user.testScore.size}")
    @Max(value = 9, message = "{user.testScore.size}")
    private int introversion;
}
