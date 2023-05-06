package hous.notification.config.sqs.producer;

import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.MessageAttributeValue;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import hous.common.constant.MessageType;
import hous.notification.config.sqs.dto.MessageDto;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SqsProducer {
	@Value("${cloud.aws.sqs.api.url}")
	private String url;

	private static final String messageGroupId = "sqs";

	private final ObjectMapper objectMapper;
	private final AmazonSQS amazonSQS;

	public SqsProducer(ObjectMapper objectMapper, AmazonSQS amazonSQS) {
		this.objectMapper = objectMapper;
		this.amazonSQS = amazonSQS;
	}

	public void produce(MessageDto dto) {
		try {
			SendMessageRequest sendMessageRequest = new SendMessageRequest(url, objectMapper.writeValueAsString(dto))
				.withMessageGroupId(messageGroupId)
				.withMessageDeduplicationId(UUID.randomUUID().toString())
				.withMessageAttributes(createMessageAttributes(dto.getType()));
			amazonSQS.sendMessage(sendMessageRequest);
			log.info(String.format("====> [SQS Queue Request] : %s ",
				dto));
		} catch (JsonProcessingException exception) {
			log.error(exception.getMessage(), exception);
		}
	}

	private Map<String, MessageAttributeValue> createMessageAttributes(String type) {
		final String STRING = "String";
		return Map.of(MessageType.TYPE, new MessageAttributeValue()
			.withDataType(STRING)
			.withStringValue(type));
	}
}
