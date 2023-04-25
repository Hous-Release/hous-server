package hous.api.service.auth.dto.request;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TokenRequestDto {

	@ApiModelProperty(value = "토큰 - accessToken", example = "eyJhbGciOiJIUzUxMiJ9.udnKnDSK08EuX56E5k-")
	@NotBlank(message = "{auth.accessToken.notBlank}")
	private String accessToken;

	@ApiModelProperty(value = "토큰 - refreshToken", example = "eyJhbGciOiJIUzUxMiJ9.udnKnDSK08EuX56E5k-")
	@NotBlank(message = "{auth.refreshToken.notBlank}")
	private String refreshToken;
}
