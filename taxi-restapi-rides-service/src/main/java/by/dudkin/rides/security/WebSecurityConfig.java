package by.dudkin.rides.security;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import static by.dudkin.rides.utils.RideEndpoints.*;

/**
 * @author Alexander Dudkin
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

    private final ObjectMapper objectMapper;

    public WebSecurityConfig(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Bean
    public SecurityFilterChain jwtFilterChain(HttpSecurity http) throws Exception {

        http.oauth2ResourceServer(oauth2 -> oauth2
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
