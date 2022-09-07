package hous.server.service.user.dto.request;


import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UpdateUserInfoRequestDto extends SetOnboardingInfoRequestDto {

    @ApiModelProperty(value = "MBTI", example = "CUTE")
    @NotBlank(message = "{user.mbti.notBlank}")
    @Size(min = 1, max = 4, message = "{user.mbti.max}")
    private String mbti;

    @ApiModelProperty(value = "직업", example = "대학생")
    @NotBlank(message = "{user.job.notBlank}")
    @Size(min = 1, max = 3, message = "{user.job.max}")
    private String job;

    @ApiModelProperty(value = "자기소개", example = "안녕하세요. 저는 혜조니입니다~")
    @NotBlank(message = "{user.introduction.notBlank}")
    @Size(min = 1, max = 40, message = "{user.introduction.max}")
    private String introduction;
}
