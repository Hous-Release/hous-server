package hous.api.service.rule.dto.response;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;

import hous.common.util.DateUtils;
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
public class RulesResponse {
	private List<RuleInfo> rules;

	public static RulesResponse of(List<Rule> rules) {
		return RulesResponse.builder()
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

		private boolean isNew;

		private String createdAt;

		@JsonProperty("isNew")
		public boolean isNew() {
			return isNew;
		}

		public static RuleInfo of(Rule rule) {
			return RuleInfo.builder()
				.id(rule.getId())
				.name(rule.getName())
				.isNew(DateUtils.todayLocalDateTime().isBefore(rule.getCreatedAt().plusHours(12)))
				.createdAt(rule.getCreatedAt().toString())
				.build();
		}
	}
}
