package hous.server.service.slack.dto.response;


import lombok.*;

import java.util.List;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class UserDeleteResponse {

    private int totalDeleteUserCount;
    private List<UserDelete> totalDeleteUserList;
    private String currentDeleteUserFeedbackType;

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder(access = AccessLevel.PRIVATE)
    public static class UserDelete {
        private int cnt;
        private String feedbackType;

        public static UserDelete of(int cnt, String feedbackType) {
            return UserDelete.builder()
                    .cnt(cnt)
                    .feedbackType(feedbackType)
                    .build();
        }
    }

    public static UserDeleteResponse of(int totalDeleteUserCount, List<UserDelete> totalDeleteUserList, String currentDeleteUserFeedbackType) {
        return UserDeleteResponse.builder()
                .totalDeleteUserCount(totalDeleteUserCount)
                .totalDeleteUserList(totalDeleteUserList)
                .currentDeleteUserFeedbackType(currentDeleteUserFeedbackType)
                .build();
    }
}
