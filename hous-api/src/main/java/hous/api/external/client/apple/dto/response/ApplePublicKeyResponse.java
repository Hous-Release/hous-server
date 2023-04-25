package hous.api.external.client.apple.dto.response;

import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApplePublicKeyResponse {

	private List<Key> keys;

	public Key getMatchedPublicKey(String kid, String alg) {
		return keys.stream()
			.filter(key -> key.getKid().equals(kid) && key.getAlg().equals(alg))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("일치하는 Public Key가 존재하지 않습니다"));
	}

	@ToString
	@Getter
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	// @checkstyle:off 애플 로그인 response 로 받는 형태라 한글자 멤버 변수 사용 필요
	public static class Key {
		private String alg;
		private String e;
		private String kid;
		private String kty;
		private String n;
		private String use;
	}
}
