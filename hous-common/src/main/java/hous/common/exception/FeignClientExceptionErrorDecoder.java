package hous.common.exception;

import feign.Response;
import feign.codec.ErrorDecoder;
import feign.codec.StringDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Slf4j
@Configuration
public class FeignClientExceptionErrorDecoder implements ErrorDecoder {
    private StringDecoder stringDecoder = new StringDecoder();

    @Override
    public FeignClientException decode(String methodKey, Response response) {
        String message = null;
        if (response.body() != null) {
            try {
                message = stringDecoder.decode(response, String.class).toString();
            } catch (IOException e) {
                log.error(methodKey + "Error Deserializing response body from failed feign request response.", e);
            }
        }
        return new FeignClientException(response.status(), message);
    }
}
