package hous.api.service.rule.dto.response;

import java.util.List;
import java.util.stream.Collectors;

import hous.core.domain.rule.Rule;
import hous.core.domain.rule.RuleImage;
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
public class RuleInfoResponse {

	private Long id;
	private String name;
	private String description;
	private List<String> images;
	private String updatedAt;

	public static RuleInfoResponse of(Rule rule) {
		return RuleInfoResponse.builder()
			.id(rule.getId())
			.name(rule.getName())
			.description(rule.getDescription())
			.images(rule.getImages().stream().map(RuleImage::getImageS3Url).collect(Collectors.toList()))
			.updatedAt(rule.getUpdatedAt().toString())
			.build();
	}
}
