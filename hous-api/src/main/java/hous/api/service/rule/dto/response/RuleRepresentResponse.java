package hous.api.service.rule.dto.response;

import java.util.List;
import java.util.stream.Collectors;

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
public class RuleRepresentResponse {

	private List<RuleInfo> rules;

	public static RuleRepresentResponse of(List<Rule> rules) {
		return RuleRepresentResponse.builder()
			.rules(rules.stream()
				.sorted(Rule::compareTo)
				.map(RuleInfo::of).collect(Collectors.toList()))
			.build();
	}

	@Getter
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	@Builder(access = AccessLevel.PRIVATE)
	private static class RuleInfo {
		private Long id;
		private String name;
		private boolean isRepresent;

		public static RuleInfo of(Rule rule) {
			return RuleInfo.builder()
				.id(rule.getId())
				.name(rule.getName())
				.isRepresent(rule.isRepresent())
				.build();
		}
	}
}
