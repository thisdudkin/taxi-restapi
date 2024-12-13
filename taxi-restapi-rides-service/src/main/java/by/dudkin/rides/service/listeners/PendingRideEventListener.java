package by.dudkin.rides.service.listeners;

import by.dudkin.rides.kafka.producer.RideRequestProducer;
import by.dudkin.rides.rest.dto.request.PendingRide;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author Alexander Dudkin
 */
@Component
public class PendingRideEventListener {

    private final RideRequestProducer rideRequestProducer;

    public PendingRideEventListener(RideRequestProducer rideRequestProducer) {
        this.rideRequestProducer = rideRequestProducer;
    }

    @EventListener
    public void handleRideRequestEvent(PendingRide event) {
        rideRequestProducer.sendMessage(event);
    }

}
