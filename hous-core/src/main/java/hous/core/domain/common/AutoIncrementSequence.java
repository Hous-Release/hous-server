package hous.core.domain.common;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document(collection = "auto_sequence")
public class AutoIncrementSequence {

	@Id
	private String id;

	@Field(name = "sequence")
	private Long sequence;
}
