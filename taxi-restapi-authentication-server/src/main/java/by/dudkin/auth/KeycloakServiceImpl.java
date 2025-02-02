package by.dudkin.auth;

import by.dudkin.auth.i18n.I18nUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.ws.rs.core.Response;
import java.util.Collections;

/**
 * @author Alexander Dudkin
 */
@Service
class KeycloakServiceImpl implements KeycloakService {

    final RealmResource realmResource;
    final KeycloakProperties properties;
    final CreationService creationService;
    final I18nUtils i18nUtils;

    KeycloakServiceImpl(RealmResource realmResource, KeycloakProperties properties, CreationService creationService, I18nUtils i18nUtils) {
        this.properties = properties;
        this.realmResource = realmResource;
        this.creationService = creationService;
        this.i18nUtils = i18nUtils;
    }

    @Override
    public void save(RegistrationRequest req) {
        UserRepresentation userRepresentation = creationService.configureRepresentation(req);

        try (Response response = realmResource.users().create(userRepresentation)) {
            validateResponse(response);
        }

        String userId = realmResource.users().search(req.username()).getFirst().getId();
        UserResource user = realmResource.users().get(userId);

        assignRole(user, req.role());
    }

    @Override
    public AccessTokenResponse login(LoginRequest req) {
        try (Keycloak keycloak = KeycloakBuilder.builder()
                 .serverUrl(properties.serverUrl())
                 .realm(properties.realm())
                 .grantType(OAuth2Constants.PASSWORD)
                 .clientId(properties.clientId())
                 .clientSecret(properties.clientSecret())
                 .username(req.username())
                 .password(req.password())
                 .build()) {
            return keycloak.tokenManager().getAccessToken();
        }
    }

    void validateResponse(Response response) {
        int status = response.getStatus();

        if (status != HttpStatus.CREATED.value()) {
            String msg = response.getStatusInfo().getReasonPhrase();

            try {
                String responseBody = response.readEntity(String.class);
                JsonNode jsonNode = new ObjectMapper().readTree(responseBody);

                msg = jsonNode.path("errorMessage").asText(msg);
            } catch (Exception e) {
                msg = i18nUtils.getMessage(ErrorMessages.KEYCLOAK_RESPONSE_ERROR);
            }

            throw new ResponseStatusException(HttpStatus.valueOf(status), msg);
        }
    }

    void assignRole(UserResource user, Role role) {
        RoleRepresentation roleRepresentation = realmResource.roles().get(role.toString()).toRepresentation();
        user.roles().realmLevel().add(Collections.singletonList(roleRepresentation));
    }

}
