package hous.server.service.rule.dto.response;

import hous.server.domain.rule.Rule;
import lombok.*;

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
