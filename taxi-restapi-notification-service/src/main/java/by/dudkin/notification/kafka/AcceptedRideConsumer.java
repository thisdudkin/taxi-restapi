package by.dudkin.notification.kafka;

import by.dudkin.common.util.KafkaConstants;
import by.dudkin.notification.kafka.domain.AcceptedRide;
import by.dudkin.notification.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @author Alexander Dudkin
 */
@Slf4j
@Component
public class AcceptedRideConsumer {

    private final NotificationService notificationService;

    public AcceptedRideConsumer(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @KafkaListener(topics = KafkaConstants.ACCEPTED_RIDES_TOPIC)
    public void consume(AcceptedRide ride) {
        log.info("Json message received -> {}", ride.toString());
        notificationService.removeAcceptedRide(ride);
    }

}
