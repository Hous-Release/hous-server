package hous.server.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateUtils {

    public static LocalDate toLocalDate(String date) {
        return LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
    }

    public static String todayLocalDateToString() {
        return LocalDate.now(ZoneId.of("Asia/Seoul")).toString();
    }

    public static LocalDate todayLocalDate() {
        return LocalDate.now(ZoneId.of("Asia/Seoul"));
    }

    public static LocalDateTime todayLocalDateTime() {
        return LocalDateTime.now(ZoneId.of("Asia/Seoul"));
    }

    public static LocalDate yesterdayLocalDate() {
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Seoul"));
        return today.minusDays(1);
    }

    public static String parseMonthAndDay(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM.dd");
        return date.format(formatter);
    }

    public static String parseYearAndMonthAndDay(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        return date.format(formatter);
    }

    public static String nowDayOfWeek(LocalDate now) {
        return now.getDayOfWeek().toString();
    }

    public static boolean isSameDate(LocalDateTime localDateTime, LocalDate localDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return localDateTime.format(formatter).equals(localDate.format(formatter));
    }

    public static String passedTime(LocalDateTime now, LocalDateTime createdAt) {
        Duration duration = Duration.between(createdAt, now);
        long seconds = duration.getSeconds();
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        long weeks = days / 7;
        long months = days / 30;
        long years = days / 365;
        if (years != 0) return years + "년 전";
        if (months != 0) return months + "달 전";
        if (weeks != 0) return weeks + "주 전";
        if (days != 0) return days + "일 전";
        if (hours != 0) return hours + "시간 전";
        if (minutes != 0) return minutes + "분 전";
        return "방금";
    }
}
