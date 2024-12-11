package by.dudkin.rides.configuration;

import by.dudkin.common.util.KafkaConstants;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;

/**
 * @author Alexander Dudkin
 */
@Configuration
public class RideProducerConfiguration {

    @Bean
    public NewTopic rideRequestsTopic() {
        return TopicBuilder
            .name(KafkaConstants.RIDE_REQUESTS_TOPIC)
            .partitions(KafkaConstants.PARTITIONS_COUNT)
            .replicas(KafkaConstants.REPLICAS_COUNT)
            .build();
    }

}
