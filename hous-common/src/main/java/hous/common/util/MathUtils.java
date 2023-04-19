package hous.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MathUtils {

    public static int percent(int part, int total) {
        return (int) ((double) part / (double) total * 100);
    }

    public static int getAge(LocalDate date) {
        LocalDate today = DateUtils.todayLocalDate();
        return today.getYear() - date.getYear() + 1;
    }
}
