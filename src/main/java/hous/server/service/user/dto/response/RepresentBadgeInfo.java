package hous.server.service.user.dto.response;

import hous.server.domain.badge.Represent;
import lombok.*;

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
        if (represent == null) return null;
        return RepresentBadgeInfo.builder()
                .badgeId(represent.getBadge().getId())
                .name(represent.getBadge().getInfo().getValue())
                .imageUrl(represent.getBadge().getInfo().getImageUrl())
                .build();
    }
}
