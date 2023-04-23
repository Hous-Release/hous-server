package hous.core.domain.notification;

import hous.core.service.mongodb.SequenceGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
public class NotificationListener extends AbstractMongoEventListener<Notification> {

    private final SequenceGeneratorService generatorService;

    @Override
    public void onBeforeConvert(BeforeConvertEvent<Notification> event) {
        if (event.getSource().getId() == null) {
            event.getSource().setId(generatorService.generateSequence(Notification.SEQUENCE_NAME));
        }
        if (event.getSource().getCreatedAt() == null) {
            event.getSource().setCreatedAt(LocalDateTime.now());
        }
    }
}
