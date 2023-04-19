package hous.core.domain.notification;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum NotificationMessage {

    NEW_RULE("Rules가 추가되었어요!"),
    NEW_TODO("to-do가 새로 추가되었어요!"),
    NEW_TODO_TAKE("to-do의 담당자가 되었어요!"),
    TODO_REMIND("to-do가 완료되지 않았어요!"),
    NEW_BADGE("배지를 얻었어요!");

    private final String value;

    public String getKey() {
        return name();
    }
}
