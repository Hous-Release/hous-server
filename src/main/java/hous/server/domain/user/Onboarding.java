package hous.server.domain.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import hous.server.domain.badge.Badge;
import hous.server.domain.badge.Represent;
import hous.server.domain.common.AuditingTimeEntity;
import hous.server.domain.personality.Personality;
import hous.server.domain.room.Participate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Column(nullable = false)
    private boolean isChecked;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "personality_id", nullable = false)
    private Personality personality;

    @OneToOne(mappedBy = "onboarding", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Represent represent;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "test_score_id")
    private TestScore testScore;

    @OneToMany(mappedBy = "onboarding", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Participate> participates = new ArrayList<>();

    @OneToMany(mappedBy = "onboarding", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Tag> tags = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "badge_id")
    private final List<Badge> badges = new ArrayList<>();

    @Builder(access = AccessLevel.PRIVATE)
    public Onboarding(String nickname, LocalDate birthday, String introduction, boolean isChecked, Personality personality, Represent represent, TestScore testScore) {
        this.nickname = nickname;
        this.birthday = birthday;
        this.introduction = introduction;
        this.isChecked = isChecked;
        this.personality = personality;
        this.represent = represent;
        this.testScore = testScore;
    }

    public static Onboarding newInstance(Personality personality) {
        return Onboarding.builder()
                .isChecked(false)
                .personality(personality)
                .build();
    }

    public void setOnboarding(String nickname, LocalDate birthday) {
        this.nickname = nickname;
        this.birthday = birthday;
        this.isChecked = true;
    }

    public void addParticipate(Participate participate) {
        this.participates.add(participate);
    }
}
