package hous.server.domain.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import hous.server.domain.badge.Badge;
import hous.server.domain.badge.Represent;
import hous.server.domain.common.AuditingTimeEntity;
import hous.server.domain.personality.Personality;
import hous.server.domain.room.Participate;
import hous.server.service.user.dto.request.UpdateUserInfoRequestDto;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class Onboarding extends AuditingTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 30)
    private String nickname;

    @Column
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate birthday;

    @Column(length = 30)
    private String introduction;

    @Column(length = 30)
    private String mbti;

    @Column(length = 30)
    private String job;

    @Column(nullable = false)
    private boolean isChecked;

    @Column(nullable = false)
    private boolean isPublic;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "personality_id", nullable = false)
    private Personality personality;

    @OneToOne(mappedBy = "onboarding", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Represent represent;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "test_score_id", nullable = false)
    private TestScore testScore;

    @OneToMany(mappedBy = "onboarding", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Participate> participates = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "badge_id")
    private final List<Badge> badges = new ArrayList<>();

    public static Onboarding newInstance(Personality personality, TestScore testScore) {
        return Onboarding.builder()
                .isChecked(false)
                .personality(personality)
                .testScore(testScore)
                .build();
    }

    public void setOnboarding(String nickname, LocalDate birthday, boolean isPublic) {
        this.nickname = nickname;
        this.birthday = birthday;
        this.isPublic = isPublic;
        this.isChecked = true;
    }

    public void addParticipate(Participate participate) {
        this.participates.add(participate);
    }

    public void deleteParticipate(Participate participate) {
        this.participates.remove(participate);
    }

    public void setUserInfo(UpdateUserInfoRequestDto request) {
        this.nickname = request.getNickname();
        this.isPublic = request.isPublic();
        this.birthday = request.getBirthday();
        this.mbti = request.getMbti();
        this.job = request.getJob();
        this.introduction = request.getIntroduction();
    }

    public void resetUserInfo() {
        this.isPublic = false;
        this.mbti = null;
        this.job = null;
        this.introduction = null;
    }

    public void resetBadge() {
        this.represent = null;
        this.badges.clear();
    }

    public void resetTestScore(TestScore testScore) {
        this.testScore = testScore.resetScore(testScore);
    }
}
