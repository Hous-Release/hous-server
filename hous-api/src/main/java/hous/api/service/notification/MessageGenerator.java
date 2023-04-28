package hous.api.service.notification;

public interface MessageGenerator {
	default String generateContent(String name, String message) {
		return String.format("'%s' %s", name, message);
	}

	default String generateDetailContent(String name, String message) {
		return String.format("%s%s", name, message);
	}
}
