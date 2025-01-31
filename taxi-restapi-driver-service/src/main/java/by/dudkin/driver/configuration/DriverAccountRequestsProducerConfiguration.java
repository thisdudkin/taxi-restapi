package by.dudkin.driver.configuration;

import by.dudkin.common.util.KafkaConstants;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

/**
 * @author Alexander Dudkin
 */
@Configuration
public class DriverAccountRequestsProducerConfiguration {

    @Bean
    public NewTopic driverAccountsTopic() {
        return TopicBuilder
            .name(KafkaConstants.DRIVER_ACCOUNT_REQUESTS_TOPIC)
            .partitions(KafkaConstants.PARTITIONS_COUNT)
            .replicas(KafkaConstants.REPLICAS_COUNT)
            .build();
    }

}
