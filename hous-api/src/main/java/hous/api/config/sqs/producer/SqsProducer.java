package hous.api.config.sqs.producer;

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
import hous.common.dto.sqs.MessageDto;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SqsProducer {
	@Value("${cloud.aws.sqs.notification.url}")
	private String url;

	@Value(value = "${spring.profiles.active}")
	String profile;

	private static final String messageGroupId = "sqs";

	private final ObjectMapper objectMapper;
	private final AmazonSQS amazonSqs;

	public SqsProducer(ObjectMapper objectMapper, AmazonSQS amazonSqs) {
		this.objectMapper = objectMapper;
		this.amazonSqs = amazonSqs;
	}

	public void produce(MessageDto dto) {
		try {
			if (!profile.equals("local")) {
				SendMessageRequest sendMessageRequest = new SendMessageRequest(url,
					objectMapper.writeValueAsString(dto))
					.withMessageGroupId(messageGroupId)
					.withMessageDeduplicationId(UUID.randomUUID().toString())
					.withMessageAttributes(createMessageAttributes(dto.getType()));
				amazonSqs.sendMessage(sendMessageRequest);
				log.info(String.format("====> [SQS Queue Request] : %s ",
					dto));
			}
		} catch (JsonProcessingException exception) {
			log.error(exception.getMessage(), exception);
		}
	}

	private Map<String, MessageAttributeValue> createMessageAttributes(String type) {
		final String dataType = "String";
		return Map.of(MessageType.TYPE, new MessageAttributeValue()
			.withDataType(dataType)
			.withStringValue(type));
	}
}
