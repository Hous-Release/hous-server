package hous.api.external.client.kakao;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import hous.api.external.client.kakao.dto.response.KakaoProfileResponse;

@FeignClient(name = "kakaoApiClient", url = "https://kapi.kakao.com")
public interface KakaoApiClient {

	@GetMapping("/v2/user/me")
	KakaoProfileResponse getProfileInfo(@RequestHeader("Authorization") String accessToken);
}
