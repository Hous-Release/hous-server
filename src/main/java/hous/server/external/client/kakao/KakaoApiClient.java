package hous.server.external.client.kakao;

import hous.server.external.client.kakao.dto.response.KakaoProfileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "kakaoApiClient", url = "https://kapi.kakao.com")
public interface KakaoApiClient {

    @GetMapping("/v2/user/me")
    KakaoProfileResponse getProfileInfo(@RequestHeader("Authorization") String accessToken);
}
