package hous.server.service.slack.dto.response;


import lombok.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class UserDeleteResponse {

    private long totalDeleteUserCount;
    private List<UserDelete> totalDeleteUserList;

    public static UserDeleteResponse of(long totalDeleteUserCount, List<UserDelete> users) {
        return UserDeleteResponse.builder()
                .totalDeleteUserCount(totalDeleteUserCount)
                .totalDeleteUserList(users.stream()
                        .map(user -> UserDelete.of(user.getCount(), user.getFeedbackType()))
                        .sorted(Comparator.comparing(userDelete -> userDelete.getFeedbackType().length()))
                        .collect(Collectors.toList())
                )
                .build();
    }
}
