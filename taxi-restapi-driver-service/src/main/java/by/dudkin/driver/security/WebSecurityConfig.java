package by.dudkin.driver.security;

import by.dudkin.driver.util.AssignmentEndpoints;
import by.dudkin.driver.util.CarEndpoints;
import by.dudkin.driver.util.DriverEndpoints;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author Alexander Dudkin
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

    private final ObjectMapper objectMapper;

    public WebSecurityConfig(MessageSource messageSource, ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Bean
    public SecurityFilterChain jwtFilterChain(HttpSecurity http) throws Exception {

        http.oauth2ResourceServer((oauth2) -> oauth2
                .jwt(jwtConfigurer -> jwtConfigurer.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                .accessDeniedHandler(accessDeniedHandler())
                .authenticationEntryPoint(authenticationEntryPoint())
        );

        http.authorizeHttpRequests(swagger -> swagger
            .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/actuator/**").permitAll()
            .requestMatchers("/api-docs/openapi.yml").permitAll()
            .requestMatchers("/swagger-resources/**").permitAll()
            .requestMatchers("/webjars/**").permitAll()
        );

        http.authorizeHttpRequests((authz) -> authz

            // driver endpoints
            .requestMatchers(HttpMethod.GET, DriverEndpoints.GET_ALL_DRIVERS.getURI()).hasAnyRole("DRIVER", "ADMIN")
            .requestMatchers(HttpMethod.POST, DriverEndpoints.SAVE_DRIVER.getURI()).hasAnyRole("DRIVER", "ADMIN")
            .requestMatchers(HttpMethod.GET, DriverEndpoints.GET_DRIVER.getURI()).hasAnyRole("DRIVER", "ADMIN", "PASSENGER")
            .requestMatchers(HttpMethod.PUT, DriverEndpoints.UPDATE_DRIVER.getURI()).hasAnyRole("DRIVER", "ADMIN")
            .requestMatchers(HttpMethod.DELETE, DriverEndpoints.DELETE_DRIVER.getURI()).hasRole("ADMIN")
            .requestMatchers(HttpMethod.PUT, DriverEndpoints.UPDATE_DRIVER_LOCATION.getURI()).hasRole("DRIVER")

            // car endpoints
            .requestMatchers(HttpMethod.GET, CarEndpoints.BASE_URI.getURI()).hasAnyRole("DRIVER", "ADMIN")
            .requestMatchers(HttpMethod.POST, CarEndpoints.SAVE_CAR.getURI()).hasAnyRole("DRIVER", "ADMIN")
            .requestMatchers(HttpMethod.GET, CarEndpoints.GET_CAR.getURI()).hasAnyRole("DRIVER", "ADMIN", "PASSENGER")
            .requestMatchers(HttpMethod.PUT, CarEndpoints.UPDATE_CAR.getURI()).hasAnyRole("DRIVER", "ADMIN")
            .requestMatchers(HttpMethod.DELETE, CarEndpoints.DELETE_CAR.getURI()).hasRole("ADMIN")

            // assignments endpoints
            .requestMatchers(HttpMethod.GET, AssignmentEndpoints.BASE_URI.getURI()).hasAnyRole("DRIVER", "ADMIN")
            .requestMatchers(HttpMethod.POST, AssignmentEndpoints.SAVE_ASSIGNMENT.getURI()).hasAnyRole("DRIVER", "ADMIN")
            .requestMatchers(HttpMethod.GET, AssignmentEndpoints.GET_ASSIGNMENT.getURI()).hasAnyRole("DRIVER", "ADMIN", "PASSENGER")
            .requestMatchers(HttpMethod.PUT, AssignmentEndpoints.UPDATE_ASSIGNMENT.getURI()).hasAnyRole("DRIVER", "ADMIN")
            .requestMatchers(HttpMethod.DELETE, AssignmentEndpoints.DELETE_ASSIGNMENT.getURI()).hasRole("ADMIN")

            .anyRequest().authenticated()
        );

        return http.build();

    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers("/v3/api-docs")
            .requestMatchers("/swagger-resources/**")
            .requestMatchers("/swagger-ui.html")
            .requestMatchers("/configuration/**")
            .requestMatchers("/webjars/**")
            .requestMatchers("/public");
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter authenticationConverter = new JwtAuthenticationConverter();
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        authenticationConverter.setPrincipalClaimName("preferred_username");
        authenticationConverter.setJwtGrantedAuthoritiesConverter(jwt -> {
            Collection<GrantedAuthority> authorities = grantedAuthoritiesConverter.convert(jwt);
            List<String> roles = jwt.getClaimAsStringList("spring_sec_roles");

            return Stream.concat(authorities.stream(),
                            roles.stream()
                                    .filter(role -> role.startsWith("ROLE_"))
                                    .map(SimpleGrantedAuthority::new)
                                    .map(GrantedAuthority.class::cast))
                    .toList();
        });

        return authenticationConverter;
    }

    @Bean
    AccessDeniedHandler accessDeniedHandler() {
        return ((request, response, accessDeniedException) -> {
            ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, accessDeniedException.getMessage());

            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setContentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE);

            String json = objectMapper.writeValueAsString(detail);
            response.getWriter().write(json);
        });
    }

    @Bean
    AuthenticationEntryPoint authenticationEntryPoint() {
        return ((request, response, authException) -> {
            ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, authException.getMessage());

            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE);

            String json = objectMapper.writeValueAsString(detail);
            response.getWriter().write(json);
        });
    }

}
