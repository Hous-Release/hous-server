package hous.server.service.user.dto.response;

import hous.server.domain.badge.Acquire;
import hous.server.domain.badge.Badge;
import hous.server.domain.badge.Represent;
import lombok.*;

import java.util.ArrayList;
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

    public static MyBadgeInfoResponse of(Represent represent, List<Badge> badgeList, List<Acquire> myBadges) {
        RepresentBadgeInfo representBadge = represent != null ? RepresentBadgeInfo.of(represent) : null;
        List<BadgeInfo> badges = !badgeList.isEmpty() ? BadgeInfo.of(badgeList, myBadges) : new ArrayList<>();
        return MyBadgeInfoResponse.builder()
                .representBadge(representBadge)
                .badges(badges)
                .build();
    }

    @ToString
    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class BadgeInfo extends RepresentBadgeInfo {
        private boolean acquire;
        private String description;

        @Builder(access = AccessLevel.PRIVATE)
        public BadgeInfo(Long badgeId, String name, String imageUrl, boolean acquire, String description) {
            super(badgeId, name, imageUrl);
            this.acquire = acquire;
            this.description = description;
        }

        private static BadgeInfo toBadgeInfo(Badge badge, boolean acquire) {
            return BadgeInfo.builder()
                    .badgeId(badge.getId())
                    .name(badge.getInfo().getValue())
                    .imageUrl(badge.getInfo().getImageUrl())
                    .acquire(acquire)
                    .description(badge.getInfo().getDescription())
                    .build();
        }

        public static BadgeInfo of(BadgeInfo badgeInfo, boolean acquire) {
            return BadgeInfo.builder()
                    .badgeId(badgeInfo.getBadgeId())
                    .name(badgeInfo.getName())
                    .imageUrl(badgeInfo.getImageUrl())
                    .acquire(acquire)
                    .description(badgeInfo.getDescription())
                    .build();
        }

        public static List<BadgeInfo> of(List<Badge> badgesList, List<Acquire> acquireBadgesList) {
            List<BadgeInfo> acquireBadgeInfos = acquireBadgesList.stream()
                    .map(acquire -> toBadgeInfo(acquire.getBadge(), true))
                    .collect(Collectors.toList());

            List<BadgeInfo> badgeInfos = badgesList.stream()
                    .map(badge -> toBadgeInfo(badge, false))
                    .collect(Collectors.toList());

            List<BadgeInfo> badges = new ArrayList<>();
            for (int i = 0; i < badgeInfos.size(); i++) {
                badges.add(BadgeInfo.of(badgeInfos.get(i), false));
                for (int j = 0; j < acquireBadgeInfos.size(); j++) {
                    if (badgeInfos.get(i).getBadgeId().equals(acquireBadgeInfos.get(j).getBadgeId())) {
                        badges.set(i, BadgeInfo.of(acquireBadgeInfos.get(j), true));
                    }
                }
            }
            return badges;
        }
    }
}
