package hous.core.domain.todo;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum DayOfWeek {
    MONDAY("월", 1),
    TUESDAY("화", 2),
    WEDNESDAY("수", 3),
    THURSDAY("목", 4),
    FRIDAY("금", 5),
    SATURDAY("토", 6),
    SUNDAY("일", 7);

    private final String value;

    private final int index;

    public String getKey() {
        return name();
    }

    public static String toString(Set<DayOfWeek> dayOfWeekSet) {
        return dayOfWeekSet.stream()
                .sorted(Comparator.comparing(DayOfWeek::getIndex))
                .map(DayOfWeek::getValue)
                .collect(Collectors.joining(", "));
    }

    public static String getValueByIndex(int index) {
        switch (index) {
            case 1:
                return MONDAY.getValue();
            case 2:
                return TUESDAY.getValue();
            case 3:
                return WEDNESDAY.getValue();
            case 4:
                return THURSDAY.getValue();
            case 5:
                return FRIDAY.getValue();
            case 6:
                return SATURDAY.getValue();
            case 7:
                return SUNDAY.getValue();
        }
        return null;
    }
}
