package hous.api.service.rule.dto.response;

import hous.core.domain.rule.Rule;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class RuleInfoResponse {

    private List<RuleInfo> rules;

    public static RuleInfoResponse of(List<Rule> rules) {
        return RuleInfoResponse.builder()
                .rules(rules.stream()
                        .sorted(Rule::compareTo)
                        .map(RuleInfo::of).collect(Collectors.toList()))
                .build();
    }
}
