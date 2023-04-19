package hous.common.util;

import lombok.Getter;

import java.util.UUID;

@Getter
public class UuidUtils {

    private static final String VERSION = "v1";

    public static String generate() {
        return String.format("%s-%s", VERSION, UUID.randomUUID().toString());
    }
}
