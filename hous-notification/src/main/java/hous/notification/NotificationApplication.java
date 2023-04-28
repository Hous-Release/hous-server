package hous.notification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = "hous.external")
@EntityScan("hous.core")
@SpringBootApplication(scanBasePackages = {"hous.core", "hous.common", "hous.external", "hous.notification"})
public class NotificationApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotificationApplication.class, args);
	}
}
