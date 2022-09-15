package hous.server.service.user.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import hous.server.domain.badge.Badge;
import hous.server.domain.badge.Represent;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class MyBadgeInfoResponse {
    private RepresentBadgeInfo representBadge;
    private List<BadgeInfo> badges;

    public static MyBadgeInfoResponse of(Represent represent, List<Badge> badgeList, List<Badge> myBadges) {
        return MyBadgeInfoResponse.builder()
                .representBadge(RepresentBadgeInfo.of(represent))
                .badges(BadgeInfo.of(badgeList, myBadges))
                .build();
    }

    @ToString
    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class BadgeInfo extends RepresentBadgeInfo {
        private boolean isAcquired;
        private String description;

        @JsonProperty("isAcquired")
        public boolean isAcquired() {
            return isAcquired;
        }

        @Builder(access = AccessLevel.PRIVATE)
        public BadgeInfo(Long badgeId, String name, String imageUrl, boolean isAcquired, String description) {
            super(badgeId, name, imageUrl);
            this.isAcquired = isAcquired;
            this.description = description;
        }

        public static BadgeInfo of(Badge badge, boolean isAcquired) {
            return BadgeInfo.builder()
                    .badgeId(badge.getId())
                    .name(badge.getInfo().getValue())
                    .imageUrl(badge.getInfo().getImageUrl())
                    .isAcquired(isAcquired)
                    .description(badge.getInfo().getDescription())
                    .build();
        }

        public static List<BadgeInfo> of(List<Badge> badges, List<Badge> myBadges) {
            return badges.stream()
                    .map(badge -> BadgeInfo.of(badge, myBadges.contains(badge)))
                    .collect(Collectors.toList());
        }
    }
}
