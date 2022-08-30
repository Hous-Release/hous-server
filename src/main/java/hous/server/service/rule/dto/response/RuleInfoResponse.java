package hous.server.service.rule.dto.response;

import hous.server.domain.rule.Rule;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class RuleInfoResponse {

    private Long id;

    private int index;

    private String name;

    public static RuleInfoResponse of(Rule rule) {
        return RuleInfoResponse.builder()
                .id(rule.getId())
                .index(rule.getIdx())
                .name(rule.getName())
                .build();
    }

    public static List<RuleInfoResponse> of(List<Rule> rules) {
        return rules.stream().map(RuleInfoResponse::of).collect(Collectors.toList());
    }
}
