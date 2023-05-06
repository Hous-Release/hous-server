package hous.core.domain.personality;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import hous.core.domain.common.AuditingTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Personality extends AuditingTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 30)
	private String name;

	@Column(nullable = false, length = 300)
	private String imageUrl;

	@Column(nullable = false, length = 30)
	@Enumerated(EnumType.STRING)
	private PersonalityColor color;

	@Column(nullable = false, length = 300)
	private String title;

	@Column(nullable = false, length = 500)
	private String description;

	@Column(nullable = false, length = 300)
	private String recommendTitle;

	@Column(nullable = false, length = 300)
	private String recommendTodo;

	@Column(nullable = false, length = 300)
	private String goodPersonalityName;

	@Column(nullable = false, length = 300)
	private String goodPersonalityImageUrl;

	@Column(nullable = false, length = 300)
	private String badPersonalityName;

	@Column(nullable = false, length = 300)
	private String badPersonalityImageUrl;
}
