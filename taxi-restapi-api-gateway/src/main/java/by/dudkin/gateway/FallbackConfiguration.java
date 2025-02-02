package by.dudkin.gateway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * @author Alexander Dudkin
 */
@Configuration
class FallbackConfiguration {

    @Bean
    RouterFunction<ServerResponse> routerFunction() {
        RequestPredicate notGetMethod = RequestPredicates.method(HttpMethod.GET).negate();

        return RouterFunctions
            .route(RequestPredicates.GET("/passenger-fallback"),
                this::handleGetFallback)
            .andRoute(notGetMethod.and(RequestPredicates.path("/passenger-fallback")),
                this::handleFallback)
            .andRoute(RequestPredicates.GET("/driver-fallback"),
                this::handleGetFallback)
            .andRoute(notGetMethod.and(RequestPredicates.path("/driver-fallback")),
                this::handleFallback)
            .andRoute(RequestPredicates.GET("/rides-fallback"),
                this::handleGetFallback)
            .andRoute(notGetMethod.and(RequestPredicates.path("/rides-fallback")),
                this::handleFallback)
            .andRoute(RequestPredicates.GET("/auth-fallback"),
                this::handleGetFallback)
            .andRoute(notGetMethod.and(RequestPredicates.path("/auth-fallback")),
                this::handleFallback)
            .andRoute(RequestPredicates.GET("/payment-fallback"),
                this::handleGetFallback)
            .andRoute(notGetMethod.and(RequestPredicates.path("/payment-fallback")),
                this::handleFallback);
    }

    Mono<ServerResponse> handleGetFallback(ServerRequest serverRequest) {
        return ServerResponse.ok().body(Mono.empty(), String.class);
    }

    Mono<ServerResponse> handleFallback(ServerRequest serverRequest) {
        return ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE).build();
    }

}
