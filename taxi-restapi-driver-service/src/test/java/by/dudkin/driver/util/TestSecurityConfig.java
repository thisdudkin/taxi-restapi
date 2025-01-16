package by.dudkin.driver.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.time.Instant;
import java.util.List;
import java.util.Map;

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
            .requestMatchers(DriverEndpoints.BASE_URI.getAllURIs()).hasAnyRole("DRIVER", "ADMIN")
            .requestMatchers(CarEndpoints.BASE_URI.getAllURIs()).hasAnyRole("DRIVER", "ADMIN")
            .requestMatchers(AssignmentEndpoints.BASE_URI.getAllURIs()).hasAnyRole("DRIVER", "ADMIN")
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
