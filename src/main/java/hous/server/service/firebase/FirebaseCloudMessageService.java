package hous.server.service.firebase;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import hous.server.common.exception.FeignClientException;
import hous.server.common.exception.InternalServerException;
import hous.server.common.util.HttpHeaderUtils;
import hous.server.common.util.JwtUtils;
import hous.server.common.util.YamlPropertySourceFactory;
import hous.server.domain.user.User;
import hous.server.domain.user.repository.UserRepository;
import hous.server.external.client.firebase.FirebaseApiClient;
import hous.server.service.firebase.dto.request.FcmMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

import static hous.server.common.exception.ErrorCode.INTERNAL_SERVER_EXCEPTION;

@Slf4j
@RequiredArgsConstructor
@Service
@PropertySource(value = "classpath:application-firebase.yml", factory = YamlPropertySourceFactory.class, ignoreResourceNotFound = true)
public class FirebaseCloudMessageService {

    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    private final FirebaseApiClient firebaseApiCaller;
    private final JwtUtils jwtProvider;

    public void sendMessageTo(String targetToken, String title, String body) {
        String message = makeMessage(targetToken, title, body);
        try {
            firebaseApiCaller.requestFcmMessaging(HttpHeaderUtils.withBearerToken(getAccessToken()), message);
        } catch (FeignClientException exception) {
            User user = userRepository.findUserByFcmToken(targetToken);
            if (user != null) {
                jwtProvider.expireRefreshToken(user.getId());
            }
            log.error(exception.getErrorMessage(), exception);
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
                        .token(targetToken)
                        .build())
                .build();
        try {
            return objectMapper.writeValueAsString(fcmMessage);
        } catch (JsonProcessingException exception) {
            log.error(exception.getMessage(), exception);
            throw new InternalServerException("FCM makeMessage exception", INTERNAL_SERVER_EXCEPTION);
        }
    }

    private String getAccessToken() {
        String firebaseConfigPath = "firebase/firebase_service_key.json";
        try {
            GoogleCredentials googleCredentials = GoogleCredentials
                    .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                    .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));
            googleCredentials.refreshIfExpired();
            return googleCredentials.getAccessToken().getTokenValue();
        } catch (IOException exception) {
            log.error(exception.getMessage(), exception);
            throw new InternalServerException("FCM getAccessToken exception", INTERNAL_SERVER_EXCEPTION);
        }
    }
}
