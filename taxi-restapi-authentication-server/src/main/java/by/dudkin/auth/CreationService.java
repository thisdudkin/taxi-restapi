package by.dudkin.auth;

import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @author Alexander Dudkin
 */
@Service
class CreationService {

    UserRepresentation configureRepresentation(RegistrationRequest req) {
        UserRepresentation userRepresentation = new UserRepresentation();

        userRepresentation.setEnabled(true);
        userRepresentation.setUsername(req.username());
        userRepresentation.setEmail(req.email());
        userRepresentation.setEmailVerified(true);

        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();

        credentialRepresentation.setValue(req.password());
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        userRepresentation.setCredentials(Collections.singletonList(credentialRepresentation));

        return userRepresentation;
    }

}
