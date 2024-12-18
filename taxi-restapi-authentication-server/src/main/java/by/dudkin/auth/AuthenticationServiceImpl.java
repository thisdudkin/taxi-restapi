package by.dudkin.auth;

import by.dudkin.common.util.ErrorMessages;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.stereotype.Service;

/**
 * @author Alexander Dudkin
 */
@Service
class AuthenticationServiceImpl implements AuthenticationService {

    final KeycloakService keycloakService;
    final ClientService clientService;

    AuthenticationServiceImpl(KeycloakService keycloakService, ClientService clientService) {
        this.keycloakService = keycloakService;
        this.clientService = clientService;
    }

    @Override
    public void saveUser(RegistrationRequest req) {
        if (req.role() != Role.ROLE_ADMIN) {
            keycloakService.save(req);
        } else {
            throw new IllegalArgumentException(ErrorMessages.NOT_ALLOWED_ROLE);
        }
    }

    @Override
    public void saveAdmin(RegistrationRequest req) {
        keycloakService.save(req);
    }

    @Override
    public AccessTokenResponse login(LoginRequest req) {
        return keycloakService.login(req);
    }

    @Override
    public AccessTokenResponse refreshToken(String token) {
        return clientService.refreshToken(token);
    }

}
