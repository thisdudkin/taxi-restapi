package by.dudkin.passenger.configuration;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.binder.MeterBinder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static by.dudkin.passenger.util.MetricUtils.*;

/**
  * @author Alexander Dudkin
  */
@Configuration
public class MetricConfig {

    @Bean
    public MeterBinder meterBinder() {
        return registry -> {
            Counter.builder(PASSENGERS_CREATED_COUNT)
                .description(PASSENGERS_CREATED_COUNT_DESCRIPTION)
                .register(registry);

            Counter.builder(PASSENGERS_UPDATED_COUNT)
                .description(PASSENGERS_UPDATED_COUNT_DESCRIPTION)
                .register(registry);
        };
    }

}
