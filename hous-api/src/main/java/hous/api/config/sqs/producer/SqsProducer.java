package hous.api.config.sqs.producer;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import hous.api.config.sqs.dto.MessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Component
@Slf4j
public class SqsProducer {
	@Value("${cloud.aws.sqs.queue.url}")
	private String url;

	private final ObjectMapper objectMapper;
	private final AmazonSQS amazonSQS;

	public void produce(MessageDto dto) {
		try {
			SendMessageRequest sendMessageRequest = new SendMessageRequest(url, objectMapper.writeValueAsString(dto))
				.withMessageGroupId("sqs")
				.withMessageDeduplicationId(UUID.randomUUID().toString());
			amazonSQS.sendMessage(sendMessageRequest);
		} catch (JsonProcessingException exception) {
			log.error(exception.getMessage(), exception);
		}
	}
}
