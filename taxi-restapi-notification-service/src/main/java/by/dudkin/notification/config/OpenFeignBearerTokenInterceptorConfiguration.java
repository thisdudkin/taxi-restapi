package by.dudkin.notification.config;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

/**
 * @author Alexander Dudkin
 */
@Configuration
public class OpenFeignBearerTokenInterceptorConfiguration {

    private static final String BEARER = "Bearer ";

    @Bean
    RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            final String authorization = HttpHeaders.AUTHORIZATION;
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication instanceof JwtAuthenticationToken jwtAuthenticationToken) {
                String token = jwtAuthenticationToken.getToken().getTokenValue();
                requestTemplate.header(authorization, BEARER + token);
            }
        };
    }

}
