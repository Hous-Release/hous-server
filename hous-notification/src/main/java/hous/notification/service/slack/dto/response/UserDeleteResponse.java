package hous.notification.service.slack.dto.response;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class UserDeleteResponse {

	private long totalDeleteUserCount;
	private List<UserDelete> totalDeleteUserList;
	private String comment;

	public static UserDeleteResponse of(long totalDeleteUserCount, List<UserDelete> users, String comment) {
		return UserDeleteResponse.builder()
			.totalDeleteUserCount(totalDeleteUserCount)
			.totalDeleteUserList(users.stream()
				.map(user -> UserDelete.of(user.getCount(), user.getFeedbackType()))
				.sorted(Comparator.comparing(userDelete -> userDelete.getFeedbackType().length()))
				.collect(Collectors.toList())
			)
			.comment(comment)
			.build();
	}
}
