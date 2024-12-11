package by.dudkin.rides.kafka.producer;

import by.dudkin.common.util.KafkaConstants;
import by.dudkin.rides.kafka.domain.AcceptedRideEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

/**
 * @author Alexander Dudkin
 */
@Slf4j
@Component
public class AcceptedRideProducer {

    private final KafkaTemplate<String, AcceptedRideEvent> kafkaTemplate;

    public AcceptedRideProducer(KafkaTemplate<String, AcceptedRideEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(AcceptedRideEvent ride) {
        log.info("Json message send -> {}", ride.toString());
        Message<AcceptedRideEvent> message = MessageBuilder
            .withPayload(ride)
            .setHeader(KafkaHeaders.TOPIC, KafkaConstants.ACCEPTED_RIDES_TOPIC)
            .build();
        kafkaTemplate.send(message);
    }

}
