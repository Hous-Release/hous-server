package hous.server.domain.notification;

import hous.server.service.mongodb.SequenceGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class NotificationListener extends AbstractMongoEventListener<NotificationMongo> {

    private final SequenceGeneratorService generatorService;

    @Override
    public void onBeforeConvert(BeforeConvertEvent<NotificationMongo> event) {
        event.getSource().setId(generatorService.generateSequence(NotificationMongo.SEQUENCE_NAME));
    }
}
