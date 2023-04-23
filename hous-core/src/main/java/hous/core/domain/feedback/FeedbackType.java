package hous.core.domain.feedback;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum FeedbackType {
    NO("없음"),
    DONE_LIVING_TOGETHER("공동생활이 끝나서"),
    INCONVENIENT_TO_USE("이용이 불편하고 장애가 많아서"),
    LOW_USAGE("사용 빈도가 낮아서"),
    CONTENTS_UNSATISFACTORY("콘텐츠 불만"),
    ETC("기타");

    private final String value;

    public String getKey() {
        return name();
    }
}
