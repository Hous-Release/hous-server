package hous.api.notification;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hous.api.service.notification.NotificationService;
import hous.api.service.notification.dto.request.NotificationSendAllRequestDto;
import hous.common.dto.SuccessResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@Api(tags = "Notification")
@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class NotificationController {

	private final NotificationService notificationService;

	@ApiOperation(
		value = "마지막 API 입니당. ㅠㅠ 혜정이 혁준이 고생했어요.",
		notes = "앱 종료 푸시 알림을 전송합니다."
	)
	@PostMapping("/notification/all")
	public ResponseEntity<SuccessResponse<String>> sendAll(@Valid @RequestBody NotificationSendAllRequestDto request) {
		notificationService.sendAll(request);
		return SuccessResponse.OK;
	}
}
