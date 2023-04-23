package hous.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HttpHeaderUtils {

    private static final String BEARER_TOKEN = "Bearer ";

    public static String withBearerToken(String token) {
        return BEARER_TOKEN.concat(token);
    }
}
