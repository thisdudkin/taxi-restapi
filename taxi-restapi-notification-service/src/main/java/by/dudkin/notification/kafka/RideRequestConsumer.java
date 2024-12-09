package by.dudkin.notification.kafka;

import by.dudkin.common.util.KafkaConstants;
import by.dudkin.notification.domain.RideRequest;
import by.dudkin.notification.dto.PendingRide;
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

    @KafkaListener(topics = KafkaConstants.RIDE_REQUESTS_TOPIC)
    public void consume(PendingRide ride) {
        RideRequest req = new RideRequest(null, ride.rideId(),
            ride.from().getLat(), ride.from().getLng(), ride.price());
        log.info("Json message received -> {}", req.toString());
        rideDao.insertRideRequest(req);
    }

}
