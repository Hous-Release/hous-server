package hous.notification.config.sqs.consumer;

import java.util.Map;

import org.springframework.cloud.aws.messaging.listener.Acknowledgment;
import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import hous.common.constant.MessageType;
import hous.notification.config.sqs.dto.FirebaseDto;
import hous.notification.config.sqs.dto.SlackExceptionDto;
import hous.notification.config.sqs.dto.SlackUserDeleteDto;
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

	@SqsListener(value = "${cloud.aws.sqs.notification.name}", deletionPolicy = SqsMessageDeletionPolicy.NEVER)
	public void consume(@Payload String info, @Headers Map<String, String> headers, Acknowledgment ack) {
		try {
			switch (headers.get(MessageType.TYPE)) {
				case MessageType.FIREBASE:
					FirebaseDto firebaseDto = objectMapper.readValue(info, FirebaseDto.class);
					firebaseCloudMessageService.sendMessageTo(
						firebaseDto.getFcmToken(), firebaseDto.getTitle(), firebaseDto.getBody());
					break;
				case MessageType.SLACK_EXCEPTION:
					SlackExceptionDto slackExceptionDto = objectMapper.readValue(info, SlackExceptionDto.class);
					slackService.sendSlackMessageProductError(
						slackExceptionDto.getInstance(), slackExceptionDto.getException());
					break;
				case MessageType.SLACK_USER_DELETE:
					SlackUserDeleteDto slackUserDeleteDto = objectMapper.readValue(info, SlackUserDeleteDto.class);
					slackService.sendSlackMessageDeleteUser(slackUserDeleteDto.getUserDeleteResponse());
					break;
			}
		} catch (Exception exception) {
			log.error(exception.getMessage(), exception);
		}
		ack.acknowledge();
	}
}
