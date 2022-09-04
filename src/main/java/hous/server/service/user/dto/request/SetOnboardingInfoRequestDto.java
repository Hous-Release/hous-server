package hous.server.service.user.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class SetOnboardingInfoRequestDto {

    @ApiModelProperty(value = "닉네임", example = "혜조니")
    @NotBlank(message = "{onboarding.nickname.notBlank}")
    @Size(min = 1, max = 5, message = "{onboarding.nickname.max}")
    private String nickname;

    @ApiModelProperty(value = "생년월일", example = "1999-03-04")
    @NotNull(message = "{onboarding.birthday.notNull}")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate birthday;

    @ApiModelProperty(value = "생년월일 공개 여부", example = "true")
    @NotNull(message = "{onboarding.isPublic.notNull}")
    private Boolean isPublic;

    @JsonProperty("isPublic")
    public Boolean isPublic() {
        return isPublic;
    }
}
