package by.dudkin.auth;

import org.keycloak.representations.AccessTokenResponse;

/**
 * @author Alexander Dudkin
 */
interface KeycloakService {
    void save(RegistrationRequest req);
    AccessTokenResponse login(LoginRequest req);
}
