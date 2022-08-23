package hous.server.domain.user;

import hous.server.domain.common.AuditingTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends AuditingTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private SocialInfo socialInfo;

    @Column(nullable = false, length = 300)
    private String fcmToken;

    @Column(nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "onboarding_id", nullable = false)
    private Onboarding onboarding;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "setting_id", nullable = false)
    private Setting setting;

    private User(String socialId, UserSocialType socialType, String fcmToken) {
        this.socialInfo = SocialInfo.of(socialId, socialType);
        this.fcmToken = fcmToken;
        this.status = UserStatus.ACTIVE;
    }

    public static User newInstance(String socialId, UserSocialType socialType, String fcmToken) {
        return new User(socialId, socialType, fcmToken);
    }
}
