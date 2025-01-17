package by.dudkin.metric;

import io.micrometer.core.instrument.MeterRegistry;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Collections;

/**
 * @author Alexander Dudkin
 */
@Aspect
@Component
public class MetricTrackingAspect {

    private final MeterRegistry meterRegistry;

    public MetricTrackingAspect(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @Before("@annotation(trackMetric)")
    public void trackMetric(TrackMetric trackMetric) {
        this.meterRegistry.counter(trackMetric.metricName(), Collections.emptyList()).increment();
    }

}
