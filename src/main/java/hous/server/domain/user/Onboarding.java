package hous.server.domain.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import hous.server.domain.badge.Acquire;
import hous.server.domain.badge.Represent;
import hous.server.domain.common.AuditingTimeEntity;
import hous.server.domain.notification.Notification;
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
public class Onboarding extends AuditingTimeEntity implements Comparable<Onboarding> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 30)
    private String nickname;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate birthday;

    @Column(length = 30)
    private String introduction;

    @Column(length = 30)
    private String mbti;

    @Column(length = 30)
    private String job;

    @Column(nullable = false)
    private boolean isPublic;

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
    private final List<Acquire> acquires = new ArrayList<>();

    @OneToMany(mappedBy = "onboarding", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Notification> notifications = new ArrayList<>();

    public static Onboarding newInstance(User user, Personality personality, String nickname, LocalDate birthday, boolean isPublic) {
        return Onboarding.builder()
                .user(user)
                .personality(personality)
                .nickname(nickname)
                .birthday(birthday)
                .isPublic(isPublic)
                .build();
    }

    public void updateRepresent(Represent represent) {
        this.represent = represent;
    }

    public void updatePersonality(Personality personality) {
        this.personality = personality;
    }

    public void addParticipate(Participate participate) {
        this.participates.add(participate);
    }

    public void addAcquire(Acquire acquire) {
        this.acquires.add(acquire);
    }

    public void addNotification(Notification notification) {
        this.notifications.add(notification);
    }

    public void deleteParticipate(Participate participate) {
        this.participates.remove(participate);
    }

    public void setTestScore(TestScore testScore) {
        this.testScore = testScore;
    }

    public void updateUserInfo(UpdateUserInfoRequestDto request) {
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
        this.testScore = null;
    }

    public void resetBadge() {
        this.represent = null;
        this.acquires.clear();
    }

    @Override
    public int compareTo(Onboarding o) {
        TestScore t1 = getTestScore();
        TestScore t2 = o.getTestScore();
        if (t1 == null && t2 != null) {
            return 1;
        }
        if (t1 != null && t2 == null) {
            return -1;
        }
        if (t1 == null && t2 == null) {
            Participate p1 = getParticipates().get(0);
            Participate p2 = o.getParticipates().get(0);
            return p1.getCreatedAt().compareTo(p2.getCreatedAt());
        }
        return t1.getCreatedAt().compareTo(t2.getCreatedAt());
    }
}
