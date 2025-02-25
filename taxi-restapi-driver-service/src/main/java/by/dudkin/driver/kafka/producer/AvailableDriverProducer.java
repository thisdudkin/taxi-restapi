package by.dudkin.driver.kafka.producer;

import by.dudkin.common.util.KafkaConstants;
import by.dudkin.driver.rest.dto.request.AvailableDriver;
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
public class AvailableDriverProducer {

    private final KafkaTemplate<String, AvailableDriver> kafkaTemplate;

    public void sendMessage(AvailableDriver driver) {
        log.info("Json message send -> {}", driver.toString());
        Message<AvailableDriver> message = MessageBuilder
            .withPayload(driver)
            .setHeader(KafkaHeaders.TOPIC, KafkaConstants.AVAILABLE_DRIVERS_TOPIC)
            .build();
        kafkaTemplate.send(message);
    }

}
