package by.dudkin.driver.configuration;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.binder.MeterBinder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static by.dudkin.driver.util.MetricUtils.ASSIGNMENTS_CANCELLED_COUNT;
import static by.dudkin.driver.util.MetricUtils.ASSIGNMENTS_CANCELLED_COUNT_DESCRIPTION;
import static by.dudkin.driver.util.MetricUtils.ASSIGNMENTS_CREATED_COUNT;
import static by.dudkin.driver.util.MetricUtils.ASSIGNMENTS_CREATED_COUNT_DESCRIPTION;
import static by.dudkin.driver.util.MetricUtils.ASSIGNMENTS_DELETED_COUNT;
import static by.dudkin.driver.util.MetricUtils.ASSIGNMENTS_DELETED_COUNT_DESCRIPTION;
import static by.dudkin.driver.util.MetricUtils.CARS_CREATED_COUNT;
import static by.dudkin.driver.util.MetricUtils.CARS_CREATED_COUNT_DESCRIPTION;
import static by.dudkin.driver.util.MetricUtils.CARS_DELETED_COUNT;
import static by.dudkin.driver.util.MetricUtils.CARS_DELETED_COUNT_DESCRIPTION;
import static by.dudkin.driver.util.MetricUtils.DRIVERS_CREATED_COUNT;
import static by.dudkin.driver.util.MetricUtils.DRIVERS_CREATED_COUNT_DESCRIPTION;
import static by.dudkin.driver.util.MetricUtils.DRIVERS_LOCATION_UPDATED_COUNT;
import static by.dudkin.driver.util.MetricUtils.DRIVERS_LOCATION_UPDATED_COUNT_DESCRIPTION;
import static by.dudkin.driver.util.MetricUtils.DRIVERS_UPDATED_COUNT;
import static by.dudkin.driver.util.MetricUtils.DRIVERS_UPDATED_COUNT_DESCRIPTION;

/**
  * @author Alexander Dudkin
  */
@Configuration
public class MetricConfig {

    @Bean
    public MeterBinder meterBinder() {
        return registry -> {
            Counter.builder(DRIVERS_CREATED_COUNT)
                .description(DRIVERS_CREATED_COUNT_DESCRIPTION)
                .register(registry);

            Counter.builder(DRIVERS_UPDATED_COUNT)
                .description(DRIVERS_UPDATED_COUNT_DESCRIPTION)
                .register(registry);

            Counter.builder(DRIVERS_LOCATION_UPDATED_COUNT)
                .description(DRIVERS_LOCATION_UPDATED_COUNT_DESCRIPTION)
                .register(registry);

            Counter.builder(CARS_CREATED_COUNT)
                .description(CARS_CREATED_COUNT_DESCRIPTION)
                .register(registry);

            Counter.builder(CARS_DELETED_COUNT)
                .description(CARS_DELETED_COUNT_DESCRIPTION)
                .register(registry);

            Counter.builder(ASSIGNMENTS_CREATED_COUNT)
                .description(ASSIGNMENTS_CREATED_COUNT_DESCRIPTION)
                .register(registry);

            Counter.builder(ASSIGNMENTS_CANCELLED_COUNT)
                .description(ASSIGNMENTS_CANCELLED_COUNT_DESCRIPTION)
                .register(registry);

            Counter.builder(ASSIGNMENTS_DELETED_COUNT)
                .description(ASSIGNMENTS_DELETED_COUNT_DESCRIPTION)
                .register(registry);
        };
    }

}
