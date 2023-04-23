package hous.api.service.user.dto.response;

import hous.common.util.DateUtils;
import hous.common.util.MathUtils;
import hous.core.domain.badge.Represent;
import hous.core.domain.personality.PersonalityColor;
import hous.core.domain.user.Onboarding;
import hous.core.domain.user.TestScore;
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

    private String introduction;

    private TestScoreResponse testScore;

    private String representBadge;

    private String representBadgeImage;

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder(access = AccessLevel.PRIVATE)
    public static class TestScoreResponse {
        private int light;

        private int noise;

        private int clean;

        private int smell;

        private int introversion;

        public static TestScoreResponse of(TestScore testScore) {
            if (testScore == null) {
                return TestScoreResponse.builder()
                        .light(0)
                        .noise(0)
                        .smell(0)
                        .clean(0)
                        .introversion(0)
                        .build();
            } else {
                return TestScoreResponse.builder()
                        .light(testScore.getLight())
                        .noise(testScore.getNoise())
                        .smell(testScore.getSmell())
                        .clean(testScore.getClean())
                        .introversion(testScore.getIntroversion())
                        .build();
            }
        }
    }


    public static UserInfoResponse of(Onboarding onboarding, Represent represent) {
        return UserInfoResponse.builder()
                .personalityColor(onboarding.getPersonality().getColor())
                .nickname(onboarding.getNickname())
                .birthdayPublic(onboarding.isPublic())
                .age(toDisplayAge(onboarding.getBirthday()))
                .birthday(toDisplayBirthday(onboarding.getBirthday()))
                .mbti(onboarding.getMbti())
                .job(onboarding.getJob())
                .mbti(onboarding.getMbti())
                .introduction(onboarding.getIntroduction())
                .testScore(TestScoreResponse.of(onboarding.getTestScore()))
                .representBadge(represent != null ? represent.getBadge().getInfo().getValue() : null)
                .representBadgeImage(represent != null ? represent.getBadge().getImageUrl() : null)
                .build();
    }

    private static String toDisplayAge(String birthday) {
        if (birthday.equals("")) {
            return birthday;
        }
        return MathUtils.getAge(DateUtils.toLocalDate(birthday)) + "세";
    }

    private static String toDisplayBirthday(String birthday) {
        if (birthday.equals("")) {
            return birthday;
        }
        return DateUtils.parseYearAndMonthAndDay(DateUtils.toLocalDate(birthday));
    }
}
