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
public class PersonalityTest extends AuditingTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private int idx;

	@Column(nullable = false, length = 300)
	private String question;

	@Column(nullable = false, length = 30)
	@Enumerated(EnumType.STRING)
	private QuestionType questionType;

	@Column(nullable = false, length = 300)
	private String answers;

	@Column(nullable = false, length = 300)
	private String imageUrl;
}
