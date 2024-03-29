package hous.api.service.user.dto.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

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
@Builder
public class UpdateTestScoreRequestDto {

	@ApiModelProperty(value = "빛", example = "4")
	@Min(value = Constraint.ONBOARDING_TESTSCORE_MIN, message = "{onboarding.testScore.size}")
	@Max(value = Constraint.ONBOARDING_TESTSCORE_MAX, message = "{onboarding.testScore.size}")
	private int light;

	@ApiModelProperty(value = "소음", example = "5")
	@Min(value = Constraint.ONBOARDING_TESTSCORE_MIN, message = "{onboarding.testScore.size}")
	@Max(value = Constraint.ONBOARDING_TESTSCORE_MAX, message = "{onboarding.testScore.size}")
	private int noise;

	@ApiModelProperty(value = "정리정돈", example = "3")
	@Min(value = Constraint.ONBOARDING_TESTSCORE_MIN, message = "{onboarding.testScore.size}")
	@Max(value = Constraint.ONBOARDING_TESTSCORE_MAX, message = "{onboarding.testScore.size}")
	private int clean;

	@ApiModelProperty(value = "냄새", example = "4")
	@Min(value = Constraint.ONBOARDING_TESTSCORE_MIN, message = "{onboarding.testScore.size}")
	@Max(value = Constraint.ONBOARDING_TESTSCORE_MAX, message = "{onboarding.testScore.size}")
	private int smell;

	@ApiModelProperty(value = "내향", example = "5")
	@Min(value = Constraint.ONBOARDING_TESTSCORE_MIN, message = "{onboarding.testScore.size}")
	@Max(value = Constraint.ONBOARDING_TESTSCORE_MAX, message = "{onboarding.testScore.size}")
	private int introversion;

	public static UpdateTestScoreRequestDto of(int light, int noise, int clean, int smell, int introversion) {
		return UpdateTestScoreRequestDto.builder()
			.light(light)
			.noise(noise)
			.clean(clean)
			.smell(smell)
			.introversion(introversion)
			.build();
	}
}
