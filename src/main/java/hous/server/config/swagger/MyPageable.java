package hous.server.config.swagger;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel
public class MyPageable {
    @ApiModelProperty(value = "페이지 번호(0...N)", required = true)
    private Integer page;

    @ApiModelProperty(value = "페이지 크기", allowableValues = "range[0,100]", required = true)
    private Integer size;

    @ApiModelProperty(value = "정렬(사용법: 칼럼명,ASC|DESC)", required = true)
    private List<String> sort;
}
