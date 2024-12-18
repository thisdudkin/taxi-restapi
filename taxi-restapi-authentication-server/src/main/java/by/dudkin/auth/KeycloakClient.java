package by.dudkin.auth;

import org.keycloak.representations.AccessTokenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

/**
 * @author Alexander Dudkin
 */
@FeignClient(
    name = "keycloak-client",
    url = "${keycloak.server-url}/realms/${keycloak.realm}/protocol/openid-connect")
interface KeycloakClient {

    @PostMapping(value = "/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    AccessTokenResponse refreshToken(@RequestBody Map<String, ?> formParams);

}
