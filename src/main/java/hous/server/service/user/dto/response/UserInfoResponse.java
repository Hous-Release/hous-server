package hous.server.service.user.dto.response;

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

    private int age;

    private boolean birthdayPublic;

    private String birthday;

    private String mbti;

    private String job;

    private boolean isSmoke;

    private String introduction;

    private TestScore testScore;

    private String representBadge;


    public static UserInfoResponse of(Onboarding onboarding, Represent represent) {
        String representBadgeImageUrl = represent != null ? represent.getBadge().getImageUrl() : null;
        return UserInfoResponse.builder()
                .personalityColor(onboarding.getPersonality().getColor())
                .nickname(onboarding.getNickname())
                .birthdayPublic(onboarding.isPublic())
                .age(MathUtils.getAge(onboarding.getBirthday()))
                .birthday(onboarding.isPublic() ? onboarding.getBirthday().toString() : null)
                .mbti(onboarding.getMbti())
                .job(onboarding.getJob())
                .isSmoke(onboarding.isSmoke())
                .mbti(onboarding.getMbti())
                .introduction(onboarding.getIntroduction())
                .testScore(onboarding.getTestScore())
                .representBadge(representBadgeImageUrl)
                .build();
    }
}
