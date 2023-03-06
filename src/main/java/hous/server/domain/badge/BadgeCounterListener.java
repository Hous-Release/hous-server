package hous.server.domain.badge;

import hous.server.service.mongodb.SequenceGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BadgeCounterListener extends AbstractMongoEventListener<BadgeCounter> {

    private final SequenceGeneratorService generatorService;

    @Override
    public void onBeforeConvert(BeforeConvertEvent<BadgeCounter> event) {
        event.getSource().setId(generatorService.generateSequence(BadgeCounter.SEQUENCE_NAME));
    }
}
