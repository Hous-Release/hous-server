package hous.api.service.user.dto.response;

import hous.core.domain.badge.Represent;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(access = AccessLevel.PRIVATE)
public class RepresentBadgeInfo {
	private Long badgeId;
	private String name;
	private String imageUrl;

	public static RepresentBadgeInfo of(Represent represent) {
		if (represent == null) {
			return null;
		}

		return RepresentBadgeInfo.builder()
			.badgeId(represent.getBadge().getId())
			.name(represent.getBadge().getInfo().getValue())
			.imageUrl(represent.getBadge().getImageUrl())
			.build();
	}
}
