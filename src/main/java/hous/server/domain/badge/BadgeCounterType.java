package hous.server.domain.badge;

import hous.server.common.model.EnumModel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum BadgeCounterType implements EnumModel {

    RULE_CREATE("RULE_CREATE"),
    TODO_COMPLETE("TODO_COMPLETE"),
    TEST_SCORE_COMPLETE("TEST_SCORE_COMPLETE");

    private final String value;

    @Override
    public String getKey() {
        return name();
    }

    @Override
    public String getValue() {
        return value;
    }
}
