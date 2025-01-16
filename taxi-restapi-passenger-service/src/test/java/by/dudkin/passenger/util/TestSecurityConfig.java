package by.dudkin.passenger.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

import static by.dudkin.passenger.util.PassengerEndpoints.DELETE_PASSENGER;
import static by.dudkin.passenger.util.PassengerEndpoints.GET_PASSENGER;
import static by.dudkin.passenger.util.PassengerEndpoints.LIST_PASSENGERS;
import static by.dudkin.passenger.util.PassengerEndpoints.SAVE_PASSENGER;
import static by.dudkin.passenger.util.PassengerEndpoints.SEARCH_PASSENGER;
import static by.dudkin.passenger.util.PassengerEndpoints.UPDATE_PASSENGER;

/**
 * @author Alexander Dudkin
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class TestSecurityConfig {

    private final ObjectMapper objectMapper;

    public TestSecurityConfig(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Bean
    SecurityFilterChain testFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);

        http.authorizeHttpRequests(auth -> auth
            .requestMatchers(HttpMethod.GET, LIST_PASSENGERS.getURI()).hasAnyRole("PASSENGER", "ADMIN")
            .requestMatchers(HttpMethod.POST, SAVE_PASSENGER.getURI()).hasAnyRole("PASSENGER", "ADMIN")
            .requestMatchers(HttpMethod.GET, SEARCH_PASSENGER.getURI()).hasAnyRole("PASSENGER", "DRIVER", "ADMIN")
            .requestMatchers(HttpMethod.GET, GET_PASSENGER.getURI()).hasAnyRole("PASSENGER", "DRIVER", "ADMIN")
            .requestMatchers(HttpMethod.PUT, UPDATE_PASSENGER.getURI()).hasAnyRole("PASSENGER", "ADMIN")
            .requestMatchers(HttpMethod.DELETE, DELETE_PASSENGER.getURI()).hasRole("ADMIN")
            .anyRequest().authenticated()
        );

        http.oauth2ResourceServer(oauth2 -> oauth2
            .jwt(Customizer.withDefaults())
            .accessDeniedHandler(testAccessDeniedHandler())
            .authenticationEntryPoint(testAuthenticationEntryPoint())
        );

        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return TestJwtUtils::parseToken;
    }

    @Bean
    AccessDeniedHandler testAccessDeniedHandler() {
        return ((request, response, accessDeniedException) -> {
            ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, accessDeniedException.getMessage());

            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setContentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE);

            String json = objectMapper.writeValueAsString(detail);
            response.getWriter().write(json);
        });
    }

    @Bean
    AuthenticationEntryPoint testAuthenticationEntryPoint() {
        return ((request, response, authException) -> {
            ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, authException.getMessage());

            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE);

            String json = objectMapper.writeValueAsString(detail);
            response.getWriter().write(json);
        });
    }

}
