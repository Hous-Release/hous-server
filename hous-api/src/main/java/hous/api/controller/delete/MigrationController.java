package hous.api.controller.delete;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hous.api.service.delete.MigrationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

// TODO migration - 삭제 예정

@Api(tags = "Migration")
@RestController
@RequestMapping("/migration")
@RequiredArgsConstructor
public class MigrationController {
	private final MigrationService migrationService;

	@ApiOperation(value = "[삭제될 api] redis 데이터 -> mongo db로 마이그레이션 하는 api.")
	@DeleteMapping("/redis")
	public ResponseEntity redisToMongoDb() {
		List<Integer> successCount = migrationService.transferRedisToMongoDb();
		return ResponseEntity.ok(
			String.format("redis 카운터 데이터 (%s)개 중 (%s)개 전환 성공", successCount.get(0), successCount.get(1)));
	}

	@ApiOperation(value = "[삭제될 api] 알림 데이터 -> mongo db로 마이그레이션 하는 api.")
	@DeleteMapping("/notification")
	public ResponseEntity notificationToMongoDb() {
		List<Integer> successCount = migrationService.transferNotificationToMongoDb();
		return ResponseEntity.ok(String.format("알림 데이터 (%s)개 중 (%s)개 전환 성공", successCount.get(0), successCount.get(1)));
	}
}
