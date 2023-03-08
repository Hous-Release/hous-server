package hous.server.service.mongodb;

import hous.server.domain.common.AutoIncrementSequence;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@RequiredArgsConstructor
@Service
public class SequenceGeneratorService {

    private final MongoOperations mongoOperations;

    public Long generateSequence(String sequenceName) {
        AutoIncrementSequence counter = mongoOperations.findAndModify(Query.query(where("_id").is(sequenceName)),
                new Update().inc("sequence", 1), options().returnNew(true).upsert(true), AutoIncrementSequence.class);
        return !Objects.isNull(counter) ? counter.getSequence() : 1;
    }
}
