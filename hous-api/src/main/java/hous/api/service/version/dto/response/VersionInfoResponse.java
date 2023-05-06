package hous.api.service.version.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class VersionInfoResponse {

	private boolean needsForceUpdate;
	private String marketUrl;

	public static VersionInfoResponse of(boolean needsForceUpdate, String marketUrl) {
		return VersionInfoResponse.builder()
			.needsForceUpdate(needsForceUpdate)
			.marketUrl(marketUrl)
			.build();
	}
}
