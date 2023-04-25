package hous.api.service.user.dto.response;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;

import hous.core.domain.badge.Badge;
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
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class MyBadgeInfoResponse {
	private RepresentBadgeInfo representBadge;
	private List<BadgeInfo> badges;

	public static MyBadgeInfoResponse of(Represent represent, List<Badge> badgeList, List<Badge> myBadges,
		List<Badge> newBadges) {
		return MyBadgeInfoResponse.builder()
			.representBadge(RepresentBadgeInfo.of(represent))
			.badges(BadgeInfo.of(badgeList, myBadges, newBadges))
			.build();
	}

	@ToString
	@Getter
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class BadgeInfo extends RepresentBadgeInfo {
		private boolean isAcquired;
		private boolean isRead;
		private String description;

		@JsonProperty("isAcquired")
		public boolean isAcquired() {
			return isAcquired;
		}

		@JsonProperty("isRead")
		public boolean isRead() {
			return isRead;
		}

		@Builder(access = AccessLevel.PRIVATE)
		public BadgeInfo(Long badgeId, String name, String imageUrl, boolean isAcquired, boolean isRead,
			String description) {
			super(badgeId, name, imageUrl);
			this.isAcquired = isAcquired;
			this.isRead = isRead;
			this.description = description;
		}

		public static BadgeInfo of(Badge badge, boolean isAcquired, boolean isRead) {
			return BadgeInfo.builder()
				.badgeId(badge.getId())
				.name(badge.getInfo().getValue())
				.imageUrl(badge.getImageUrl())
				.isAcquired(isAcquired)
				.isRead(isRead)
				.description(badge.getInfo().getDescription())
				.build();
		}

		public static List<BadgeInfo> of(List<Badge> badges, List<Badge> myBadges, List<Badge> newBadges) {
			return badges.stream()
				.map(badge -> BadgeInfo.of(badge, myBadges.contains(badge), !newBadges.contains(badge)))
				.collect(Collectors.toList());
		}
	}
}
