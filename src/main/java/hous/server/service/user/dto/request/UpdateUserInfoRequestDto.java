package hous.server.service.user.dto.request;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import hous.server.domain.common.Constraint;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UpdateUserInfoRequestDto {

    @ApiModelProperty(value = "닉네임", example = "혜조니")
    @NotBlank(message = "{onboarding.nickname.notBlank}")
    @Size(max = Constraint.ONBOARDING_NICKNAME_MAX, message = "{onboarding.nickname.max}")
    private String nickname;

    @ApiModelProperty(value = "생년월일", example = "1999-03-04")
    @NotNull(message = "{onboarding.birthday.notNull}")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate birthday;

    @ApiModelProperty(value = "생년월일 공개 여부", example = "true")
    @NotNull(message = "{onboarding.isPublic.notNull}")
    private Boolean isPublic;

    @ApiModelProperty(value = "MBTI", example = "CUTE")
    @NotBlank(message = "{onboarding.mbti.notBlank}")
    @Size(max = Constraint.ONBOARDING_MBTI_MAX, message = "{onboarding.mbti.max}")
    private String mbti;

    @ApiModelProperty(value = "직업", example = "대학생")
    @NotBlank(message = "{onboarding.job.notBlank}")
    @Size(max = Constraint.ONBOARDING_JOB_MAX, message = "{onboarding.job.max}")
    private String job;

    @ApiModelProperty(value = "자기소개", example = "안녕하세요. 저는 혜조니입니다~")
    @NotBlank(message = "{onboarding.introduction.notBlank}")
    @Size(max = Constraint.ONBOARDING_INTRODUCTION_MAX, message = "{onboarding.introduction.max}")
    private String introduction;

    @JsonProperty("isPublic")
    public Boolean isPublic() {
        return isPublic;
    }
}
