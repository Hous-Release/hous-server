package hous.api.service.user.dto.response;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import hous.core.domain.personality.Personality;
import hous.core.domain.personality.PersonalityColor;
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
public class PersonalityInfoResponse {

	private String name;
	private String imageUrl;
	private PersonalityColor color;
	private String title;
	private List<String> description;
	private String recommendTitle;
	private List<String> recommendTodo;
	private String goodPersonalityName;
	private String goodPersonalityImageUrl;
	private String badPersonalityName;
	private String badPersonalityImageUrl;

	public static PersonalityInfoResponse of(Personality personality) {
		return PersonalityInfoResponse.builder()
			.name(personality.getName())
			.imageUrl(personality.getImageUrl())
			.color(personality.getColor())
			.title(personality.getTitle())
			.description(splitToListByLineBreak(personality.getDescription()))
			.recommendTitle(personality.getRecommendTitle())
			.recommendTodo(splitToListByLineBreak(personality.getRecommendTodo()))
			.goodPersonalityName(personality.getGoodPersonalityName())
			.goodPersonalityImageUrl(personality.getGoodPersonalityImageUrl())
			.badPersonalityName(personality.getBadPersonalityName())
			.badPersonalityImageUrl(personality.getBadPersonalityImageUrl())
			.build();
	}

	private static List<String> splitToListByLineBreak(String content) {
		return Arrays.stream(content.split("\n")).collect(Collectors.toList());
	}
}
