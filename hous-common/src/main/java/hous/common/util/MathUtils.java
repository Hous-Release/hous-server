package hous.common.util;

import java.time.LocalDate;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MathUtils {

	public static int percent(int part, int total) {
		return (int)((double)part / (double)total * 100);
	}

	public static int getAge(LocalDate date) {
		LocalDate today = DateUtils.todayLocalDate();
		return today.getYear() - date.getYear() + 1;
	}
}
