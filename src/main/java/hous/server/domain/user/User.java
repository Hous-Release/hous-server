package hous.server.domain.user;

import hous.server.domain.common.AuditingTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name = "users")
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends AuditingTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private SocialInfo socialInfo;

    @Column(nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    private User(String socialId, UserSocialType socialType) {
        this.socialInfo = SocialInfo.of(socialId, socialType);
        this.status = UserStatus.ACTIVE;
    }

    public static User newInstance(String socialId, UserSocialType socialType) {
        return new User(socialId, socialType);
    }
}
