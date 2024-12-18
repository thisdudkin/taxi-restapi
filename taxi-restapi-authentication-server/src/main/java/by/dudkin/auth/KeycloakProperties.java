package by.dudkin.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Alexander Dudkin
 */
@Component
record KeycloakProperties(
    @Value("${keycloak.server-url}")
    String serverUrl,
    @Value("${keycloak.client.id}")
    String clientId,
    @Value("${keycloak.realm}")
    String realm,
    @Value("${keycloak.client.secret}")
    String clientSecret
) {}
