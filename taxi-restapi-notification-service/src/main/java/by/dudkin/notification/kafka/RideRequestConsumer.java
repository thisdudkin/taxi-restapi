package by.dudkin.notification.kafka;

import by.dudkin.notification.domain.RideRequest;
import by.dudkin.notification.service.dao.RideDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * @author Alexander Dudkin
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RideRequestConsumer {

    private final RideDao rideDao;

    @KafkaListener(topics = "${spring.kafka.topic.name.ride-requests}")
    public void consume(RideRequest request) {
        log.info("Json message received -> {}", request.toString());
        rideDao.insertRideRequest(request);
    }

}
