package hous.api.service.firebase;

import static hous.common.exception.ErrorCode.*;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;

import hous.api.service.firebase.dto.request.FcmMessage;
import hous.api.service.jwt.JwtService;
import hous.common.exception.InternalServerException;
import hous.common.util.HttpHeaderUtils;
import hous.core.domain.user.User;
import hous.external.client.firebase.FirebaseApiClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class FirebaseCloudMessageService {

	@Value("${cloud.firebase.config.path}")
	private String firebaseConfigPath;

	private final ObjectMapper objectMapper;
	private final FirebaseApiClient firebaseApiCaller;
	private final JwtService jwtService;

	public void sendMessageTo(User to, String title, String body) {
		try {
			String targetToken = to.getFcmToken();
			String message = makeMessage(targetToken, title, body);
			firebaseApiCaller.requestFcmMessaging(HttpHeaderUtils.withBearerToken(getAccessToken()), message);
		} catch (Exception exception) {
			jwtService.expireRefreshToken(to.getId());
			to.resetFcmToken();
			log.error(exception.getMessage(), exception);
		}
	}

	private String makeMessage(String targetToken, String title, String body) {
		FcmMessage fcmMessage = FcmMessage.builder()
			.validateOnly(false)
			.message(FcmMessage.Message.builder()
				.notification(FcmMessage.Notification.builder()
					.title(title)
					.body(body)
					.build())
				.android(FcmMessage.Android.builder()
					.data(FcmMessage.Data.builder()
						.title(title)
						.body(body)
						.build())
					.build())
				.apns(FcmMessage.Apns.builder()
					.payload(FcmMessage.Payload.builder()
						.aps(FcmMessage.Aps.builder()
							.alert(FcmMessage.Alert.builder()
								.title(title)
								.body(body)
								.build())
							.build())
						.build())
					.build())
				.token(targetToken)
				.build())
			.build();
		try {
			return objectMapper.writeValueAsString(fcmMessage);
		} catch (Exception exception) {
			log.error(exception.getMessage(), exception);
			throw new InternalServerException("FCM makeMessage exception", INTERNAL_SERVER_EXCEPTION);
		}
	}

	private String getAccessToken() {
		try {
			GoogleCredentials googleCredentials = GoogleCredentials
				.fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
				.createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));
			googleCredentials.refreshIfExpired();
			return googleCredentials.getAccessToken().getTokenValue();
		} catch (Exception exception) {
			log.error(exception.getMessage(), exception);
			throw new InternalServerException("FCM getAccessToken exception", INTERNAL_SERVER_EXCEPTION);
		}
	}
}
