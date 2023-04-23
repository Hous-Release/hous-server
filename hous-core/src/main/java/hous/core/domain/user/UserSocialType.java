package hous.core.domain.user;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum UserSocialType {
    APPLE("애플"),
    KAKAO("카카오톡");

    private final String value;

    public String getKey() {
        return name();
    }
}
