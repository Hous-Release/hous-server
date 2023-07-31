package hous.api.service.rule.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;

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

	public static RulesResponse of(List<Rule> rules, LocalDateTime now) {
		return RulesResponse.builder()
			.rules(rules.stream()
				.sorted(Rule::compareTo)
				.map(rule -> RuleInfo.of(rule, now)).collect(Collectors.toList()))
			.build();
	}

	@Getter
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	@Builder(access = AccessLevel.PRIVATE)
	private static class RuleInfo {

		private Long id;
		private String name;
		private boolean isNew;
		private boolean isRepresent;
		private String createdAt;

		@JsonProperty("isNew")
		public boolean isNew() {
			return isNew;
		}

		@JsonProperty("isRepresent")
		public boolean isRepresent() {
			return isRepresent;
		}

		public static RuleInfo of(Rule rule, LocalDateTime now) {
			return RuleInfo.builder()
				.id(rule.getId())
				.name(rule.getName())
				.isNew(now.isBefore(rule.getCreatedAt().plusHours(12)))
				.isRepresent(rule.isRepresent())
				.createdAt(rule.getCreatedAt().toString())
				.build();
		}
	}
}
