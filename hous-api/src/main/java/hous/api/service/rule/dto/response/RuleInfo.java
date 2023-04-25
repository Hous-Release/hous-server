package hous.api.service.rule.dto.response;

import hous.core.domain.rule.Rule;
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
public class RuleInfo {

	private Long id;

	private String name;

	public static RuleInfo of(Rule rule) {
		return RuleInfo.builder()
			.id(rule.getId())
			.name(rule.getName())
			.build();
	}
}
