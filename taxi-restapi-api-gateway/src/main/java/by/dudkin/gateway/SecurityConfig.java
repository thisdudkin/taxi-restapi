package by.dudkin.gateway;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

/**
 * @author Alexander Dudkin
 */
@Configuration
@EnableWebFluxSecurity
class SecurityConfig {

    final ObjectMapper objectMapper;

    SecurityConfig(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Bean
    SecurityWebFilterChain jwtSecurityWebFilterChain(ServerHttpSecurity http) {
        http.csrf(ServerHttpSecurity.CsrfSpec::disable);

        http.authorizeExchange(exchange -> exchange
            .pathMatchers("/api/auth/register", "/api/auth/login", "/api/auth/refresh").permitAll()
            .pathMatchers("/fallback/**").permitAll()
            .pathMatchers("/actuator/**").permitAll()
            .anyExchange().authenticated()
        );

        http.oauth2ResourceServer(oauth2 -> oauth2
            .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverterAdapter()))
            .accessDeniedHandler(reactiveAccessDeniedHandler())
            .authenticationEntryPoint(reactiveAuthenticationEntryPoint())
        );

        return http.build();
    }

    @Bean
    ReactiveJwtAuthenticationConverterAdapter jwtAuthenticationConverterAdapter() {
        AuthenticationConverter authenticationConverter = new AuthenticationConverter();
        return new ReactiveJwtAuthenticationConverterAdapter(authenticationConverter);
    }

    @Bean
    ServerAccessDeniedHandler reactiveAccessDeniedHandler() {
        return (exchange, denied) -> handleSecurityError(exchange, FORBIDDEN, denied.getMessage());
    }
    @Bean
    ServerAuthenticationEntryPoint reactiveAuthenticationEntryPoint() {
        return (exchange, ex) -> handleSecurityError(exchange, UNAUTHORIZED, ex.getMessage());
    }

    private Mono<Void> handleSecurityError(ServerWebExchange exchange, HttpStatus status, String message) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(status, message);
        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        try {
            byte[] bytes = objectMapper.writeValueAsBytes(detail);
            return exchange.getResponse().writeWith(Mono.just(exchange.getResponse()
                .bufferFactory().wrap(bytes)));
        } catch (JsonProcessingException e) {
            return Mono.error(e);
        }
    }

}
