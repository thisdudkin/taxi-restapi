package by.dudkin.rides.kafka.producer;

import by.dudkin.rides.rest.dto.request.PendingRide;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

/**
 * @author Alexander Dudkin
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RideRequestProducer {

    @Value("${spring.kafka.topic.name.ride-requests}")
    private String rideRequestsTopic;

    private final KafkaTemplate<String, PendingRide> kafkaTemplate;

    public void sendMessage(PendingRide ride) {
        log.info("Json message send -> {}", ride.toString());
        Message<PendingRide> message = MessageBuilder
            .withPayload(ride)
            .setHeader(KafkaHeaders.TOPIC, rideRequestsTopic)
            .build();
        kafkaTemplate.send(message);
    }

}
