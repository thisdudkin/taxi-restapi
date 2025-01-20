package by.dudkin.notification.metric;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.binder.MeterBinder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
  * @author Alexander Dudkin
  */
@Configuration
public class MetricConfig {

    @Bean
    public MeterBinder meterBinder() {
        return registry -> {
            Counter.builder(MetricUtils.RIDE_REQUESTS_COUNT)
                .register(registry);
        };
    }

}
