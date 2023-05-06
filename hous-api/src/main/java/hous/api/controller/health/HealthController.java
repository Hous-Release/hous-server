package hous.api.controller.health;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@RestController
public class HealthController {

	@GetMapping("/health")
	public String checkHealth() {
		return "healthy";
	}
}
