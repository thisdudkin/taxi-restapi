package by.dudkin.auth;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Alexander Dudkin
 */
@Configuration
class KeycloakConfig {

    final KeycloakProperties properties;

    KeycloakConfig(KeycloakProperties properties) {
        this.properties = properties;
    }

    @Bean
    Keycloak keycloak() {
        return KeycloakBuilder.builder()
            .serverUrl(properties.serverUrl())
            .realm(properties.realm())
            .clientId(properties.clientId())
            .clientSecret(properties.clientSecret())
            .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
            .build();
    }

    @Bean
    RealmResource realmResource(Keycloak keycloak) {
        return keycloak.realm(properties.realm());
    }

}
