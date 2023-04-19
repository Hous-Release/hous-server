package hous.api.service.slack.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class UserDelete {
    private long count;
    private String feedbackType;

    public static UserDelete of(long count, String feedbackType) {
        return UserDelete.builder()
                .count(count)
                .feedbackType(feedbackType)
                .build();
    }

    @Override
    public String toString() {
        return String.format("- %s: %s명", feedbackType, count);
    }
}
