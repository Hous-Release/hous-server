package hous.notification.config.sqs.consumer;

import java.util.Map;

import org.springframework.cloud.aws.messaging.listener.Acknowledgment;
import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import hous.common.constant.MessageType;
import hous.notification.config.sqs.dto.MessageDto;
import hous.notification.service.firebase.FirebaseCloudMessageService;
import hous.notification.service.slack.SlackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class SqsConsumer {

	private final ObjectMapper objectMapper;
	private final FirebaseCloudMessageService firebaseCloudMessageService;
	private final SlackService slackService;

	@SqsListener(value = "${cloud.aws.sqs.queue.name}", deletionPolicy = SqsMessageDeletionPolicy.NEVER)
	public void consume(@Payload String info, @Headers Map<String, String> headers, Acknowledgment ack) {
		try {
			MessageDto dto = objectMapper.readValue(info, MessageDto.class);
			switch (dto.getType()) {
				case MessageType.FIREBASE:
					firebaseCloudMessageService.sendMessageTo(dto.getTo(), dto.getTitle(), dto.getBody());
					break;
				case MessageType.SLACK_EXCEPTION:
					slackService.sendSlackMessageProductError(dto.getException());
					break;
				case MessageType.SLACK_USER_DELETE:
					slackService.sendSlackMessageDeleteUser(dto.getUserDeleteResponse());
					break;
			}
		} catch (JsonProcessingException exception) {
			log.error(exception.getMessage(), exception);
		}
		ack.acknowledge();
	}
}
