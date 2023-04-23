package hous.core.domain.badge;

import hous.core.service.mongodb.SequenceGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
public class BadgeCounterListener extends AbstractMongoEventListener<BadgeCounter> {

    private final SequenceGeneratorService generatorService;

    @Override
    public void onBeforeConvert(BeforeConvertEvent<BadgeCounter> event) {
        if (event.getSource().getId() == null) {
            event.getSource().setId(generatorService.generateSequence(BadgeCounter.SEQUENCE_NAME));
        }
        if (event.getSource().getCreatedAt() == null) {
            event.getSource().setCreatedAt(LocalDateTime.now());
        }
    }
}
