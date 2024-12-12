package by.dudkin.rides.configuration;

import by.dudkin.common.util.KafkaConstants;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

/**
 * @author Alexander Dudkin
 */
@Configuration
public class AcceptedRideProducerConfiguration {

    @Bean
    public NewTopic acceptedRidesTopic() {
        return TopicBuilder
            .name(KafkaConstants.ACCEPTED_RIDES_TOPIC)
            .partitions(KafkaConstants.PARTITIONS_COUNT)
            .replicas(KafkaConstants.REPLICAS_COUNT)
            .build();
    }

}
