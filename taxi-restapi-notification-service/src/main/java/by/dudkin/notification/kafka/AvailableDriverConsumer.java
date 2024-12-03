package by.dudkin.notification.kafka;

import by.dudkin.notification.domain.AvailableDriver;
import by.dudkin.notification.service.dao.DriverDao;
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
public class AvailableDriverConsumer {

    private final DriverDao driverDao;

    @KafkaListener(topics = "${spring.kafka.topic.name.available-drivers}")
    public void consume(AvailableDriver driver) {
        log.info("Json message received -> {}", driver.toString());
        driverDao.insertAvailableDriver(driver);
    }

}
