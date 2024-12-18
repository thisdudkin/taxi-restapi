package by.dudkin.auth;

import org.keycloak.representations.AccessTokenResponse;

/**
 * @author Alexander Dudkin
 */
interface AuthenticationService {
    void saveUser(RegistrationRequest req);
    void saveAdmin(RegistrationRequest req);
    AccessTokenResponse login(LoginRequest req);
    AccessTokenResponse refreshToken(String token);
}
