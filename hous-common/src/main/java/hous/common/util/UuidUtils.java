package hous.common.util;

import java.util.UUID;

import lombok.Getter;

@Getter
public class UuidUtils {

	private static final String VERSION = "v1";

	public static String generate() {
		return String.format("%s-%s", VERSION, UUID.randomUUID().toString());
	}
}
