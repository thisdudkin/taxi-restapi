package by.dudkin.auth;

import org.keycloak.OAuth2Constants;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Alexander Dudkin
 */
@Service
class ClientServiceImpl implements ClientService {

    final KeycloakClient keycloakClient;
    final KeycloakProperties properties;

    ClientServiceImpl(KeycloakClient keycloakClient, KeycloakProperties properties) {
        this.keycloakClient = keycloakClient;
        this.properties = properties;
    }

    @Override
    public AccessTokenResponse refreshToken(String token) {
        Map<String, String> clientReq = new HashMap<>();
        clientReq.put(OAuth2Constants.GRANT_TYPE, OAuth2Constants.REFRESH_TOKEN);
        clientReq.put(OAuth2Constants.CLIENT_ID, properties.clientId());
        clientReq.put(OAuth2Constants.CLIENT_SECRET, properties.clientSecret());
        clientReq.put(OAuth2Constants.REFRESH_TOKEN, token);

        return keycloakClient.refreshToken(clientReq);
    }

}
