package by.dudkin.driver.service.listeners;

import by.dudkin.driver.kafka.producer.DriverAccountProducer;
import by.dudkin.driver.rest.dto.request.AccountRequest;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author Alexander Dudkin
 */
@Component
public class DriverAccountListener {

    private final DriverAccountProducer driverAccountProducer;

    public DriverAccountListener(DriverAccountProducer driverAccountProducer) {
        this.driverAccountProducer = driverAccountProducer;
    }

    @EventListener
    public void handleDriverAccountEvent(AccountRequest accountRequest) {
        driverAccountProducer.sendMessage(accountRequest);
    }

}
