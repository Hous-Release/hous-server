package hous.server.domain.todo;

import hous.server.common.model.EnumModel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum DayOfWeek implements EnumModel {
    MONDAY("월", 1),
    TUESDAY("화", 2),
    WEDNESDAY("수", 3),
    THURSDAY("목", 4),
    FRIDAY("금", 5),
    SATURDAY("토", 6),
    SUNDAY("일", 7);

    private final String value;

    private final int index;

    @Override
    public String getKey() {
        return name();
    }

    @Override
    public String getValue() {
        return value;
    }

    public static String toString(Set<DayOfWeek> dayOfWeekSet) {
        return dayOfWeekSet.stream()
                .sorted(Comparator.comparing(DayOfWeek::getIndex))
                .map(DayOfWeek::getValue)
                .collect(Collectors.joining(", "));
    }
}
