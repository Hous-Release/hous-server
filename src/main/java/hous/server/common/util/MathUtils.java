package hous.server.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MathUtils {

    public static int percent(int part, int total) {
        return (int) ((double) part / (double) total * 100);
    }
}
