package by.dudkin.auth.metric;

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
            Counter.builder(MetricUtils.LOGIN_COUNT)
                .register(registry);
        };
    }

}
