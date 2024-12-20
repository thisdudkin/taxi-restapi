package by.dudkin.passenger.configuration;

import by.dudkin.passenger.util.PassengerEndpoints;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static by.dudkin.passenger.util.PassengerEndpoints.CHECK_BALANCE;
import static by.dudkin.passenger.util.PassengerEndpoints.DELETE_PASSENGER;
import static by.dudkin.passenger.util.PassengerEndpoints.SAVE_PASSENGER;
import static by.dudkin.passenger.util.PassengerEndpoints.UPDATE_BALANCE;

/**
 * @author Alexander Dudkin
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain jwtFilterChain(HttpSecurity http) throws Exception {

        http.oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults()));

        http.authorizeHttpRequests(auth -> auth
            .requestMatchers(CHECK_BALANCE.getURI(), UPDATE_BALANCE.getURI()).hasAnyRole("PASSENGER", "ADMIN")
            .requestMatchers(HttpMethod.DELETE, DELETE_PASSENGER.getURI()).hasRole("ADMIN")
            .requestMatchers(HttpMethod.POST, SAVE_PASSENGER.getURI()).hasRole("PASSENGER")
            .anyRequest().hasAnyRole("PASSENGER", "DRIVER", "ADMIN")
        );

        return http.build();

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

}
