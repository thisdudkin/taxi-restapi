package by.dudkin.driver.kafka.consumer;

import by.dudkin.driver.rest.dto.response.PendingRide;
import by.dudkin.driver.service.api.DriverService;
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

    private final DriverService driverService;

    @KafkaListener(topics = "${spring.kafka.topic.name.ride-requests}")
    public void consume(PendingRide ride) {
        log.info("Json message received -> {}", ride.toString());
        driverService.handleDriver(ride);
    }

}
