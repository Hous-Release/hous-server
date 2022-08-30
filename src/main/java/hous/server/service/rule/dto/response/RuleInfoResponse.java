package hous.server.service.rule.dto.response;

import hous.server.domain.rule.Rule;
import lombok.*;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class RuleInfoResponse {

    private int index;

    private String name;

    public static RuleInfoResponse of(Rule rule) {
        return RuleInfoResponse.builder()
                .index(rule.getIdx())
                .name(rule.getName())
                .build();
    }
}
