package hous.server.service.user.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SetOnboardingInfoRequestDto {

    @ApiModelProperty(value = "닉네임", example = "혜조니")
    @NotBlank(message = "{onboarding.nickname.notBlank}")
    @Length(min = 1, max = 5, message = "{onboarding.nickname.length}")
    private String nickname;

    @ApiModelProperty(value = "생년월일", example = "1999-03-04")
    @NotNull(message = "{onboarding.birthday.notNull}")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate birthday;
}