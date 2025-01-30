package by.dudkin.passenger.service.listeners;

import by.dudkin.passenger.kafka.PassengerAccountProducer;
import by.dudkin.passenger.rest.dto.request.AccountRequest;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author Alexander Dudkin
 */
@Component
public class PassengerAccountListener {

    private final PassengerAccountProducer passengerAccountProducer;

    public PassengerAccountListener(PassengerAccountProducer passengerAccountProducer) {
        this.passengerAccountProducer = passengerAccountProducer;
    }

    @EventListener
    public void handlePassengerAccountEvent(AccountRequest accountRequest) {
        passengerAccountProducer.sendMessage(accountRequest);
    }

}
