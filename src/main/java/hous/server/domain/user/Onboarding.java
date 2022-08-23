package hous.server.domain.user;

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
import java.time.LocalDateTime;
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
    private LocalDateTime birthday;

    @Column(length = 30)
    private String introduction;

    @Column(nullable = false)
    private boolean isChecked;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "personality_id")
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
    public Onboarding(String nickname, LocalDateTime birthday, String introduction, boolean isChecked, Personality personality, Represent represent, TestScore testScore) {
        this.nickname = nickname;
        this.birthday = birthday;
        this.introduction = introduction;
        this.isChecked = isChecked;
        this.personality = personality;
        this.represent = represent;
        this.testScore = testScore;
    }

    public static Onboarding newInstance() {
        return Onboarding.builder()
                .isChecked(false)
                .build();
    }
}
