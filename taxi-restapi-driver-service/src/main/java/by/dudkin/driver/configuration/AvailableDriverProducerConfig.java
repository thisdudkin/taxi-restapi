package by.dudkin.driver.configuration;

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
public class AvailableDriverProducerConfig {

    @Value("${kafka.partitions.count}")
    private int partitions;

    @Value("${kafka.replicas.count}")
    private int replicas;

    @Value("${spring.kafka.topic.name.available-drivers}")
    private String availableDrivers;

    @Bean
    public NewTopic topic() {
        return TopicBuilder
            .name(availableDrivers)
            .partitions(partitions)
            .replicas(replicas)
            .build();
    }

    @Bean
    public StringJsonMessageConverter stringJsonMessageConverter() {
        return new StringJsonMessageConverter();
    }

}
