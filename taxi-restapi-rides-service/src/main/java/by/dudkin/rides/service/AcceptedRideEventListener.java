package by.dudkin.rides.service;

import by.dudkin.rides.kafka.domain.AcceptedRideEvent;
import by.dudkin.rides.kafka.producer.AcceptedRideProducer;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author Alexander Dudkin
 */
@Component
public class AcceptedRideEventListener {

    private final AcceptedRideProducer acceptedRideProducer;

    public AcceptedRideEventListener(AcceptedRideProducer acceptedRideProducer) {
        this.acceptedRideProducer = acceptedRideProducer;
    }

    @EventListener
    public void handleAcceptedRideEvent(AcceptedRideEvent event) {
        acceptedRideProducer.sendMessage(event);
    }

}
