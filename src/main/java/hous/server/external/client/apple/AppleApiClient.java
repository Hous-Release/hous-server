package hous.server.external.client.apple;

import hous.server.external.client.apple.dto.response.ApplePublicKeyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "appleApiClient", url = "https://appleid.apple.com/auth")
public interface AppleApiClient {

    @GetMapping(value = "/keys")
    ApplePublicKeyResponse getAppleAuthPublicKey();
}
