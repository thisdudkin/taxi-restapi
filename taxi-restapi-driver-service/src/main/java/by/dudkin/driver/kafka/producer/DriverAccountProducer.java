package by.dudkin.driver.kafka.producer;

import by.dudkin.common.util.KafkaConstants;
import by.dudkin.driver.rest.dto.request.AccountRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class DriverAccountProducer {

    private final KafkaTemplate<String, AccountRequest> kafkaTemplate;

    public void sendMessage(AccountRequest accountRequest) {
        log.info("Json message send -> {}", accountRequest.toString());
        Message<AccountRequest> message = MessageBuilder
            .withPayload(accountRequest)
            .setHeader(KafkaHeaders.TOPIC, KafkaConstants.DRIVER_ACCOUNT_REQUESTS_TOPIC)
            .build();
        kafkaTemplate.send(message);
    }

}
