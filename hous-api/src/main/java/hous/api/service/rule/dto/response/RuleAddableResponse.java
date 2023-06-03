package hous.api.service.rule.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

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
@Builder(access = AccessLevel.PRIVATE)
public class RuleAddableResponse {

	private boolean isAddable;

	@JsonProperty("isAddable")
	public boolean isAddable() {
		return isAddable;
	}

	public static RuleAddableResponse of(boolean isAddable) {
		return RuleAddableResponse.builder()
			.isAddable(isAddable)
			.build();
	}
}
