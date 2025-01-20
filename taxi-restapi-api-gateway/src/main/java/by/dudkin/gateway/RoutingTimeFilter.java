package by.dudkin.gateway;

import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Timer;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

/**
 * @author Alexander Dudkin
 */
@Component
class RoutingTimeFilter extends AbstractGatewayFilterFactory<RoutingTimeFilter.Config> {

    RoutingTimeFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String routeId = exchange.getRequest().getURI().getHost();
            Timer.Sample sample = Timer.start();
            return chain.filter(exchange)
                .doOnTerminate(() -> {
                    sample.stop(Metrics.timer(MetricUtils.ROUTING_TIME, "route", routeId));
                });
        };
    }

    static class Config {}

}
