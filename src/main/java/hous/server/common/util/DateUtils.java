package hous.server.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateUtils {

    public static LocalDate todayLocalDate() {
        return LocalDate.now(ZoneId.of("Asia/Seoul"));
    }

    public static LocalDateTime todayLocalDateTime() {
        return LocalDateTime.now(ZoneId.of("Asia/Seoul"));
    }

    public static String parseMonthAndDay(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM.dd");
        return date.format(formatter);
    }

    public static String nowDayOfWeek(LocalDate now) {
        return now.getDayOfWeek().toString();
    }

    public static boolean isSameDate(LocalDateTime localDateTime, LocalDate localDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return localDateTime.format(formatter).equals(localDate.format(formatter));
    }

}
