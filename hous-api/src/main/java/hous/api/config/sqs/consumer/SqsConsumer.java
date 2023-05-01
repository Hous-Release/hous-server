package hous.api.config.sqs.consumer;

import java.util.Map;

import org.springframework.cloud.aws.messaging.listener.Acknowledgment;
import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import hous.api.config.sqs.dto.FcmTokenResetDto;
import hous.common.constant.MessageType;
import hous.common.util.JwtUtils;
import hous.core.domain.user.User;
import hous.core.domain.user.mysql.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class SqsConsumer {

	private final ObjectMapper objectMapper;
	private final JwtUtils jwtUtils;
	private final UserRepository userRepository;

	@Transactional
	@SqsListener(value = "${cloud.aws.sqs.api.name}", deletionPolicy = SqsMessageDeletionPolicy.NEVER)
	public void consume(@Payload String info, @Headers Map<String, String> headers, Acknowledgment ack) {
		try {
			switch (headers.get(MessageType.TYPE)) {
				case MessageType.FCM_TOKEN_RESET:
					FcmTokenResetDto fcmTokenResetDto = objectMapper.readValue(info, FcmTokenResetDto.class);
					User user = userRepository.findUserByFcmToken(fcmTokenResetDto.getFcmToken());
					if (user != null) {
						jwtUtils.expireRefreshToken(user.getId());
						user.resetFcmToken();
					}
					break;
			}
		} catch (Exception exception) {
			log.error(exception.getMessage(), exception);
		}
		ack.acknowledge();
	}
}
