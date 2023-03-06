package hous.server.domain.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@Document(collection = "auto_sequence")
public class AutoIncrementSequence {

    @Id
    private String id;

    @Field
    private Long sequence;
}
