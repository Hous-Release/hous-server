package hous.api.service.rule.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import hous.common.util.DateUtils;
import hous.core.domain.rule.Rule;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

// TODO deprecated 삭제될 api 와 response 에서만 사용되고 있어서 기존 api 삭제 시 지워주고 내부클래스 쓰기
@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class RuleInfo {

	private Long id;

	private String name;

	private boolean isNew;

	private String createdAt;

	@JsonProperty("isNew")
	public boolean isNew() {
		return isNew;
	}

	public static RuleInfo of(Rule rule) {
		return RuleInfo.builder()
			.id(rule.getId())
			.name(rule.getName())
			.isNew(DateUtils.todayLocalDateTime().isBefore(rule.getCreatedAt().plusHours(12)))
			.createdAt(rule.getCreatedAt().toString())
			.build();
	}
}
