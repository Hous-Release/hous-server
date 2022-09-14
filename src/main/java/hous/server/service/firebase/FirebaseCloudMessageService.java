package hous.server.service.firebase;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import hous.server.common.util.HttpHeaderUtils;
import hous.server.common.util.YamlPropertySourceFactory;
import hous.server.external.client.firebase.FirebaseApiClient;
import hous.server.external.client.firebase.dto.response.FcmResponse;
import hous.server.service.firebase.dto.request.FcmMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Service
@PropertySource(value = "classpath:application-firebase.yml", factory = YamlPropertySourceFactory.class, ignoreResourceNotFound = true)
public class FirebaseCloudMessageService {

    private final ObjectMapper objectMapper;

    private final FirebaseApiClient firebaseApiCaller;

    public void sendMessageTo(String targetToken, String title, String body) throws IOException {
        String message = makeMessage(targetToken, title, body);
        FcmResponse fcmResponse = firebaseApiCaller.requestFcmMessaging(HttpHeaderUtils.withBearerToken(getAccessToken()), message);
        // TODO fcmResponse.name == null 처리 추가하기
    }

    private String makeMessage(String targetToken, String title, String body) throws JsonProcessingException {
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

        return objectMapper.writeValueAsString(fcmMessage);
    }

    private String getAccessToken() throws IOException {
        String firebaseConfigPath = "firebase/firebase_service_key.json";

        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }
}
