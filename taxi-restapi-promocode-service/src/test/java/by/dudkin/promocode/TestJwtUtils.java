package by.dudkin.promocode;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author Alexander Dudkin
 */
final class TestJwtUtils {

    private static final Map<String, Jwt> tokenStorage = new ConcurrentHashMap<>();

    private TestJwtUtils() {
        throw new AssertionError();
    }

    static final String ROLE_PASSENGER = "ROLE_PASSENGER";

    static JwtAuthenticationToken createMockJwtToken(String username, String... roles) {
        Set<SimpleGrantedAuthority> authorities = Arrays.stream(roles)
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toUnmodifiableSet());

        Map<String, Object> headers = Map.of("alg", "none");
        Map<String, Object> claims = Map.of(
            TokenConstants.USERNAME_CLAIM, username,
            TokenConstants.SPRING_ROLES_CLAIM, Arrays.asList(roles)
        );

        Jwt jwt = new Jwt("mock-token", Instant.now(), Instant.now().plusSeconds(300), headers, claims);
        tokenStorage.put(jwt.getTokenValue(), jwt);

        return new JwtAuthenticationToken(jwt, authorities, username);
    }

    static HttpHeaders createHeadersWithToken(String username, String... roles) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        JwtAuthenticationToken mockJwtToken = TestJwtUtils.createMockJwtToken(username, roles);
        headers.set("Authorization", "Bearer " + mockJwtToken.getToken().getTokenValue());

        return headers;
    }

    static Jwt parseToken(String tokenValue) {
        return tokenStorage.get(tokenValue);
    }

}
