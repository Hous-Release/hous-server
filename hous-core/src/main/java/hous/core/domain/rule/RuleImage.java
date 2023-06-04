package hous.core.domain.rule;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import hous.core.domain.common.AuditingTimeEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class RuleImage extends AuditingTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "rule_id")
	private Rule rule;

	@Column(length = 255)
	private String originalName;

	@Column(length = 255, name = "image_s3_url")
	private String imageS3Url;

	public static RuleImage newInstance(Rule rule, String originalName, String imageS3Url) {
		return builder()
			.rule(rule)
			.originalName(originalName)
			.imageS3Url(imageS3Url)
			.build();
	}
}
