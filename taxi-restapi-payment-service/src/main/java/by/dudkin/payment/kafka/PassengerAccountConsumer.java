package by.dudkin.payment.kafka;

import by.dudkin.common.util.KafkaConstants;
import by.dudkin.payment.dto.AccountRequest;
import by.dudkin.payment.dto.UserType;
import by.dudkin.payment.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

/**
 * @author Alexander Dudkin
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PassengerAccountConsumer {

    private final AccountService accountService;

    @KafkaListener(topics = KafkaConstants.PASSENGER_ACCOUNT_REQUESTS_TOPIC)
    public void consume(AccountRequest accountRequest) throws SQLException {
        log.info("Json message received -> {}", accountRequest.toString());
        accountService.save(accountRequest, UserType.PASSENGER);
    }

}
