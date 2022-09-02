package hous.server.service.user.dto.response;

import hous.server.common.util.DateUtils;
import hous.server.common.util.MathUtils;
import hous.server.domain.badge.Represent;
import hous.server.domain.personality.PersonalityColor;
import hous.server.domain.user.Onboarding;
import hous.server.domain.user.TestScore;
import lombok.*;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class UserInfoResponse {

    private PersonalityColor personalityColor;

    private String nickname;

    private String age;

    private boolean birthdayPublic;

    private String birthday;

    private String mbti;

    private String job;

    private boolean isSmoke;

    private String introduction;

    private TestScore testScore;

    private String representBadge;

    private String representBadgeImage;


    public static UserInfoResponse of(Onboarding onboarding, Represent represent) {
        return UserInfoResponse.builder()
                .personalityColor(onboarding.getPersonality().getColor())
                .nickname(onboarding.getNickname())
                .birthdayPublic(onboarding.isPublic())
                .age(MathUtils.getAge(onboarding.getBirthday()) + "세")
                .birthday(onboarding.isPublic() ? DateUtils.parseMonthAndDay(onboarding.getBirthday()) : null)
                .mbti(onboarding.getMbti())
                .job(onboarding.getJob())
                .isSmoke(onboarding.isSmoke())
                .mbti(onboarding.getMbti())
                .introduction(onboarding.getIntroduction())
                .testScore(onboarding.getTestScore())
                .representBadge(represent != null ? represent.getBadge().getName() : null)
                .representBadgeImage(represent != null ? represent.getBadge().getImageUrl() : null)
                .build();
    }
}
