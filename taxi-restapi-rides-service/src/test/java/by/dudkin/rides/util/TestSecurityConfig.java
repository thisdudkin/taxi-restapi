package by.dudkin.rides.util;

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

import static by.dudkin.rides.utils.RideEndpoints.ACTIVATE_RIDE;
import static by.dudkin.rides.utils.RideEndpoints.ASSIGN_RIDE;
import static by.dudkin.rides.utils.RideEndpoints.BASE_URI;
import static by.dudkin.rides.utils.RideEndpoints.CANCEL_RIDE;
import static by.dudkin.rides.utils.RideEndpoints.CHECK_COST;
import static by.dudkin.rides.utils.RideEndpoints.COMPLETE_RIDE;
import static by.dudkin.rides.utils.RideEndpoints.DELETE_RIDE;
import static by.dudkin.rides.utils.RideEndpoints.GET_RIDE;
import static by.dudkin.rides.utils.RideEndpoints.RATE_RIDE;
import static by.dudkin.rides.utils.RideEndpoints.SAVE_RIDE;
import static by.dudkin.rides.utils.RideEndpoints.UPDATE_RIDE;

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

        http.oauth2ResourceServer(oauth2 -> oauth2
            .jwt(Customizer.withDefaults())
            .accessDeniedHandler(testAccessDeniedHandler())
            .authenticationEntryPoint(testAuthenticationEntryPoint())
        );

        http.authorizeHttpRequests(authz -> authz
            .requestMatchers(HttpMethod.GET, BASE_URI.getURI()).hasAnyRole("PASSENGER", "DRIVER", "ADMIN")
            .requestMatchers(HttpMethod.POST, SAVE_RIDE.getURI()).hasAnyRole("PASSENGER", "ADMIN")
            .requestMatchers(HttpMethod.GET, GET_RIDE.getURI()).hasAnyRole("PASSENGER", "DRIVER", "ADMIN")
            .requestMatchers(HttpMethod.POST, CHECK_COST.getURI()).hasAnyRole("PASSENGER", "ADMIN")
            .requestMatchers(HttpMethod.PUT, UPDATE_RIDE.getURI()).hasRole("ADMIN")
            .requestMatchers(HttpMethod.DELETE, DELETE_RIDE.getURI()).hasRole("ADMIN")
            .requestMatchers(HttpMethod.PATCH, ACTIVATE_RIDE.getURI()).hasAnyRole("DRIVER", "ADMIN")
            .requestMatchers(HttpMethod.POST, ASSIGN_RIDE.getURI()).hasAnyRole("DRIVER", "ADMIN")
            .requestMatchers(HttpMethod.PATCH, COMPLETE_RIDE.getURI()).hasAnyRole("DRIVER", "ADMIN")
            .requestMatchers(HttpMethod.PATCH, CANCEL_RIDE.getURI()).hasAnyRole("DRIVER", "ADMIN")
            .requestMatchers(HttpMethod.PATCH, RATE_RIDE.getURI()).hasAnyRole("PASSENGER", "ADMIN")
            .requestMatchers(HttpMethod.POST, CHECK_COST.getURI()).hasAnyRole("PASSENGER", "ADMIN")
            .anyRequest().authenticated()
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
