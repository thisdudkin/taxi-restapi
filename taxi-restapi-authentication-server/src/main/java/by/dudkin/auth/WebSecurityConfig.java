package by.dudkin.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.util.StringUtils;

import javax.ws.rs.HttpMethod;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author Alexander Dudkin
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class WebSecurityConfig {

    private final ObjectMapper objectMapper;

    WebSecurityConfig(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Bean
    public SecurityFilterChain jwtFilterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable);

        http.oauth2ResourceServer((oauth2) -> oauth2
            .jwt(jwtConfigurer -> jwtConfigurer.jwtAuthenticationConverter(jwtAuthenticationConverter()))
            .accessDeniedHandler(accessDeniedHandler())
            .authenticationEntryPoint(authenticationEntryPoint())
        );

        http.authorizeHttpRequests(authz -> authz
            .requestMatchers("/api/auth/login").permitAll()
            .requestMatchers("/api/auth/refresh").permitAll()
            .requestMatchers("/api/auth/register").permitAll()
            .requestMatchers("/api/auth/register/admin").hasRole("ADMIN")
            .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/actuator/**").permitAll()
            .requestMatchers("/api-docs/openapi.yml").permitAll()
            .requestMatchers("/swagger-resources/**").permitAll()
            .requestMatchers("/webjars/**").permitAll()
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
