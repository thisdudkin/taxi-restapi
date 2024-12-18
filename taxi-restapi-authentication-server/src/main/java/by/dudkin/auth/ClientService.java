package by.dudkin.auth;

import org.keycloak.representations.AccessTokenResponse;

/**
 * @author Alexander Dudkin
 */
interface ClientService {
    AccessTokenResponse refreshToken(String token);
}
